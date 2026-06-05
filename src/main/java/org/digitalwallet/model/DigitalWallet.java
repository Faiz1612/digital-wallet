package org.digitalwallet.model;

import org.digitalwallet.enums.Currency;

import java.time.LocalDateTime;
import java.util.List;

public class DigitalWallet {
    String userId;
    LocalDateTime createdAt;
    List<Transaction> transactionHistory;
    double totalWalletMoney;
    String walletId;

    public Currency getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Currency defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    org.digitalwallet.enums.Currency defaultCurrency;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void setTransactionHistory(List<Transaction> transactionHistory) {
        this.transactionHistory = transactionHistory;
    }

    public double getTotalWalletMoney() {
        return totalWalletMoney;
    }

    public void setTotalWalletMoney(double totalWalletMoney) {
        this.totalWalletMoney = totalWalletMoney;
    }

    public String getWalletId() {
        return walletId;
    }

    public void setWalletId(String walletId) {
        this.walletId = walletId;
    }

}
