package org.digitalwallet.service;

import org.digitalwallet.dao.TransferRequest;
import org.digitalwallet.enums.TransactionType;
import org.digitalwallet.model.*;
import org.digitalwallet.repository.TransactionDb;
import org.digitalwallet.repository.WalletDb;
import org.digitalwallet.strategy.PaymentMethodStrategy;
import org.digitalwallet.strategy.PaymentStrategyFactory;

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

    public Transaction wallerTransfer(TransferRequest transferRequest, String receiverId) {
        DigitalWallet wallet = walletDb.getWallet(transferRequest.getUserId())
                .orElseThrow(() -> new RuntimeException("no wallet exists" + "for sender, create wallet"));

        double amountInDefaultCurrency = currencyConverterService.convertMoney(transferRequest.getAmount(), transferRequest.getCurrency(),
                wallet.getDefaultCurrency());

        if(!walletDb.isWalletExist(receiverId)) {
            throw new RuntimeException("no wallet exists for receiver");
        }

        synchronized (walletDb.getWalletLock(transferRequest.getWalletId())) {
            if(wallet.getTotalWalletMoney() < amountInDefaultCurrency) {
                throw new RuntimeException("not enough money in wallet");
            }

            DigitalWallet receiverWallet = walletDb.getWallet(receiverId).get();
            wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() - amountInDefaultCurrency);
            depositMoneyToWallet(receiverWallet, amountInDefaultCurrency);
            WalletTransferTransactionDetails walletTransferTransactionDetails = new WalletTransferTransactionDetails(TransactionType.WALLET,
                    wallet.getWalletId(), receiverWallet.getWalletId());
            Transaction transaction = new Transaction(amountInDefaultCurrency, transferRequest.getCurrency(), TransactionType.WALLET,
                    walletTransferTransactionDetails, transferRequest.getUserId());
            transactionDb.addTransaction(transaction, transferRequest.getUserId(), receiverId);
            return transaction;
        }
    }

    private void depositMoneyToWallet(DigitalWallet wallet, double amount) {
        try {
            synchronized (walletDb.getWalletLock(wallet.getWalletId())) {
                wallet.setTotalWalletMoney(wallet.getTotalWalletMoney() + amount);
            }
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
