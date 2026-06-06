package org.digitalwallet.repository;

import org.digitalwallet.model.DigitalWallet;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class InMemoryWalletDb implements WalletDb {
    ConcurrentHashMap<String, DigitalWallet> wallets;
    ConcurrentHashMap<String, Object> walletLocks;
    ConcurrentHashMap<String, ReentrantLock> walletReentrantLocks;

    public InMemoryWalletDb() {
        this.wallets = new ConcurrentHashMap<>();
        this.walletLocks = new ConcurrentHashMap<>();
        this.walletReentrantLocks = new ConcurrentHashMap<>();
    }

    @Override
    public DigitalWallet createWallet(DigitalWallet digitalWallet) {
        wallets.put(digitalWallet.getUserId(), digitalWallet);
        walletLocks.put(digitalWallet.getWalletId(), new Object());
        walletReentrantLocks.put(digitalWallet.getWalletId(), new ReentrantLock());
        return digitalWallet;
    }

    @Override
    public Optional<DigitalWallet> getWallet(String userId) {
        return Optional.ofNullable(wallets.get(userId));
    }

    @Override
    public boolean isWalletExist(String userId) {
        return wallets.containsKey(userId);
    }

    @Override
    public Object getWalletLock(String walletId) {
        return walletLocks.get(walletId);
    }

    @Override
    public ReentrantLock getReentrantWalletLock(String walletId) {
        return walletReentrantLocks.get(walletId);
    }

    @Override
    public void updateWallet(DigitalWallet wallet, String userId) {
        wallets.put(userId, wallet);
    }
}
