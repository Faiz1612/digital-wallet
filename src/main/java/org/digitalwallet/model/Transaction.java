package org.digitalwallet.model;

import org.digitalwallet.enums.Currency;
import org.digitalwallet.enums.TransactionType;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    String transactionId;
    double amount;
    Currency currency;
    LocalDateTime createdAt;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    String userId;

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    TransactionDetails transactionDetails;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    TransactionType transactionType;

    public Transaction(double amount, Currency currency, TransactionType transactionType, TransactionDetails transactionDetails, String userId) {
        this.transactionId = UUID.randomUUID().toString();
        this.amount = amount;
        this.currency = currency;
        this.createdAt = LocalDateTime.now();
        this.transactionType = transactionType;
        this.transactionDetails = transactionDetails;
        this.userId = userId;
    }
}
