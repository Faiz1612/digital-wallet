package org.digitalwallet.repository;

import org.digitalwallet.model.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTransactionDb implements TransactionDb {
    ConcurrentHashMap<String, Transaction> transactions;
    ConcurrentHashMap<String, List<Transaction>> userTransactionHistory;
    public InMemoryTransactionDb() {
        this.transactions = new ConcurrentHashMap<>();
        this.userTransactionHistory = new ConcurrentHashMap<>();
    }

    @Override
        public Transaction addTransaction(Transaction transaction, String senderId, String receiverId) {
        transactions.put(transaction.getTransactionId(), transaction);
        userTransactionHistory.computeIfAbsent(senderId, k -> new ArrayList<>()).add(transaction);
        if(Objects.nonNull(receiverId) && !senderId.equals(receiverId)) {
            userTransactionHistory.computeIfAbsent(receiverId, k -> new ArrayList<>()).add(transaction);
        }
        return transaction;
    }

    @Override
    public Optional<Transaction> getTransaction(String transactionId) {
        if(transactions.containsKey(transactionId)) {
            return Optional.of(transactions.get(transactionId));
        }

        return Optional.empty();
    }

    @Override
    public List<Transaction> getAllTransactionForUser(String userId) {
        return userTransactionHistory.get(userId);
    }
}
