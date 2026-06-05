package org.digitalwallet.repository;

import org.digitalwallet.model.Transaction;

import java.util.List;
import java.util.Optional;

public interface TransactionDb {
    Transaction addTransaction(Transaction transaction, String senderId, String receiverId);
    Optional<Transaction> getTransaction(String transactionId);
    List<Transaction> getAllTransactionForUser(String userId);
}
