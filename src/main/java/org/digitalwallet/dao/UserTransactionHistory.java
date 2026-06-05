package org.digitalwallet.dao;

import org.digitalwallet.model.Transaction;

import java.util.List;

public class UserTransactionHistory {
    String userId;

    public UserTransactionHistory(String userId, List<UserTransactionDetails> transactionHistory) {
        this.userId = userId;
        this.transactionHistory = transactionHistory;
    }

    // other basis user details, not important here
    List<UserTransactionDetails> transactionHistory;
}
