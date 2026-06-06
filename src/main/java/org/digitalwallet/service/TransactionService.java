package org.digitalwallet.service;

import org.digitalwallet.dao.TransferRequest;
import org.digitalwallet.enums.TransactionType;
import org.digitalwallet.exception.TransactionCreationException;
import org.digitalwallet.exception.WalletTransferReceiverException;
import org.digitalwallet.exception.WalletTransferSenderException;
import org.digitalwallet.model.*;
import org.digitalwallet.repository.TransactionDb;
import org.digitalwallet.repository.WalletDb;
import org.digitalwallet.strategy.PaymentMethodStrategy;
import org.digitalwallet.strategy.PaymentStrategyFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TransactionService {
    WalletDb walletDb;
    TransactionDb transactionDb;
    CurrencyConverterService currencyConverterService;
    PaymentStrategyFactory paymentStrategyFactory;

    public TransactionService(WalletDb walletDb, TransactionDb transactionDb, CurrencyConverterService currencyConverterService, PaymentStrategyFactory paymentStrategyFactory) {
        this.walletDb = walletDb;
        this.transactionDb = transactionDb;
        this.currencyConverterService = currencyConverterService;
        this.paymentStrategyFactory = paymentStrategyFactory;
    }

    public Transaction walletTransfer(TransferRequest transferRequest, String receiverId) {
        DigitalWallet wallet = walletDb.getWallet(transferRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("No wallet exists for sender, create wallet"));

        double amountInDefaultCurrency = currencyConverterService.convertMoney(
                transferRequest.getAmount(), transferRequest.getCurrency(), wallet.getDefaultCurrency());

        if (!walletDb.isWalletExist(receiverId)) {
            throw new RuntimeException("No wallet exists for receiver");
        }

        DigitalWallet receiverWallet = walletDb.getWallet(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver wallet went missing"));

        List<ReentrantLock> reentrantLocks = new ArrayList<>();
        reentrantLocks.add(walletDb.getReentrantWalletLock(wallet.getWalletId()));
        reentrantLocks.add(walletDb.getReentrantWalletLock(receiverWallet.getWalletId()));

        boolean locksAcquired = false;
        try {
            locksAcquired = acquireAllLocks(reentrantLocks);

            if (!locksAcquired) {
                throw new RuntimeException("Could not acquire wallet locks. System busy, please try again.");
            }

            if (wallet.getTotalWalletMoney() < amountInDefaultCurrency) {
                throw new RuntimeException("Insufficient balance");
            }

            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() - amountInDefaultCurrency);
            depositMoneyToWallet(receiverWallet, amountInDefaultCurrency);

            WalletTransferTransactionDetails walletTransferTransactionDetails = new WalletTransferTransactionDetails(
                    TransactionType.WALLET, wallet.getWalletId(), receiverWallet.getWalletId());

            Transaction transaction = new Transaction(
                    amountInDefaultCurrency, transferRequest.getCurrency(), TransactionType.WALLET,
                    walletTransferTransactionDetails, transferRequest.getUserId());

            transactionDb.addTransaction(transaction, transferRequest.getUserId(), receiverId);
            walletDb.updateWallet(wallet, transferRequest.getUserId());
            return transaction;
        } catch (WalletTransferSenderException walletTransferSenderException) {
            throw new WalletTransferSenderException("failed to send money" + walletTransferSenderException.getMessage());
        }
        catch (WalletTransferReceiverException walletTransferReceiverException) {
            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() + amountInDefaultCurrency);
            throw new WalletTransferReceiverException("transfer to receiver wallet failed, reverting the state");
        } catch (TransactionCreationException transactionCreationException) {
            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() + amountInDefaultCurrency);
            depositMoneyToWallet(receiverWallet, -1*amountInDefaultCurrency);
            throw new TransactionCreationException("unable to create transaction, reverting both wallet transfers");
        } catch (RuntimeException exception) {
            throw new RuntimeException("exception occured while wallet transfer" + exception.getMessage(), exception);
        }
        finally {
            if (locksAcquired) {
                releaseAllLocks(reentrantLocks);
            }
        }
    }

    private void deductMoneyFromWallet(DigitalWallet wallet, double amount) {
        try {
            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() - amount);
        } catch (WalletTransferSenderException walletTransferSenderException) {
            throw new WalletTransferSenderException("failed to send money" + walletTransferSenderException.getMessage());
        }
    }

    boolean acquireAllLocks(List<ReentrantLock> reentrantLocks) {
        while(true) {
            boolean allAcquired = true;
            int acquiredCount = 0;
            for(int i = 0; i < reentrantLocks.size(); i++) {
                if(reentrantLocks.get(i).tryLock()) {
                    acquiredCount++;
                } else {
                    allAcquired = false;
                    break;
                }
            }

            if(allAcquired) {
                return true;
            }

            for(int i = 0; i < acquiredCount; i++) {
                reentrantLocks.get(i).unlock();
            }

            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void releaseAllLocks(List<ReentrantLock> reentrantLocks) {
        for(int i = reentrantLocks.size() - 1; i >= 0; i--) {
            if(reentrantLocks.get(i).isHeldByCurrentThread()) {
                reentrantLocks.get(i).unlock();
            }
        }
    }

    private void depositMoneyToWallet(DigitalWallet wallet, double amount) {
        try {
            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() + amount);
        } catch (Exception e) {
            throw new RuntimeException("transfer failed during sending money");
        }
    }

    public Transaction bankTransfer(TransferRequest transferRequest, String accountId) {
        DigitalWallet wallet = walletDb.getWallet(transferRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("no wallet exists" + "for sender, create wallet"));

        double amountInDefaultCurrency = currencyConverterService.convertMoney(transferRequest.getAmount(), transferRequest.getCurrency(),
                wallet.getDefaultCurrency());

        synchronized (walletDb.getWalletLock(transferRequest.getWalletId())) {
            if(wallet.getTotalWalletMoney() < amountInDefaultCurrency) {
                throw new RuntimeException("not enough money in wallet");
            }

            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() - amountInDefaultCurrency);
            // call some external api to actaul trasnfer money to bank account
            depositMoneyToBankAccount(amountInDefaultCurrency, accountId);
            BankTransferTransactionDetails bankTransferTransactionDetails = new BankTransferTransactionDetails(TransactionType.BANK_ACCOUNT,
                    wallet.getWalletId(), accountId);
            Transaction transaction = new Transaction(amountInDefaultCurrency, transferRequest.getCurrency()
                    ,TransactionType.BANK_ACCOUNT, bankTransferTransactionDetails, transferRequest.getUserId());
            transactionDb.addTransaction(transaction, transferRequest.getUserId(), null);
            return transaction;
        }
    }

    private void depositMoneyToBankAccount(double amount, String bankAccountId) {
        // call external api and catch error if comes
    }

    // wallet top up
    public Transaction topUpWallet(PaymentMethodDescription paymentMethodDescription, double amount, String userId) {
        DigitalWallet wallet = walletDb.getWallet(userId)
                .orElseThrow(() -> new RuntimeException("no wallet exists" + "for sender, create wallet"));
        synchronized (walletDb.getWalletLock(wallet.getWalletId())) {
            PaymentMethodStrategy paymentMethodStrategy = paymentStrategyFactory.getPaymentMethod(paymentMethodDescription.getPaymentMethod());
            paymentMethodStrategy.execute(paymentMethodDescription);
            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() + amount);
            SelfTransferTransactionDetails selfTransferTransactionDetails = new SelfTransferTransactionDetails(TransactionType.SELF_TRANSFER,
                    paymentMethodDescription, wallet.getWalletId());
            Transaction transaction = new Transaction(amount, wallet.getDefaultCurrency()
                    ,TransactionType.SELF_TRANSFER, selfTransferTransactionDetails, userId);
            transactionDb.addTransaction(transaction, userId, userId);
            return transaction;
        }
    }
}
