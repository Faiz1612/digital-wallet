package org.digitalwallet.service;

import org.digitalwallet.dao.UserTransactionDetails;
import org.digitalwallet.dao.UserTransactionHistory;
import org.digitalwallet.model.Transaction;
import org.digitalwallet.repository.TransactionDb;

import java.util.ArrayList;
import java.util.List;

public class TransactionManagmentService {
    TransactionDb transactionDb;

    public TransactionManagmentService(TransactionDb transactionDb) {
        this.transactionDb = transactionDb;
    }

    public UserTransactionHistory getUserTransactionHistory(String userId) {
        List<Transaction> transactions = transactionDb.getAllTransactionForUser(userId);
        List<UserTransactionDetails> transactionHistories = new ArrayList<>();
        transactions.forEach(transaction -> {
            UserTransactionDetails userTransactionDetails = new UserTransactionDetails(transaction);
            if(transaction.getUserId().equals(userId)) {
                userTransactionDetails.setDebited(false);
            }
            transactionHistories.add(userTransactionDetails);
        });

        return new UserTransactionHistory(userId, transactionHistories);
    }

}
