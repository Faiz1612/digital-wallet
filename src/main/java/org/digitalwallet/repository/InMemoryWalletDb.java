package org.digitalwallet.repository;

import org.digitalwallet.model.DigitalWallet;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryWalletDb implements WalletDb {
    ConcurrentHashMap<String, DigitalWallet> wallets;
    ConcurrentHashMap<String, Object> walletLocks;

    public InMemoryWalletDb() {
        this.wallets = new ConcurrentHashMap<>();
        this.walletLocks = new ConcurrentHashMap<>();
    }

    @Override
    public DigitalWallet createWallet(DigitalWallet digitalWallet) {
        wallets.put(digitalWallet.getUserId(), digitalWallet);
        walletLocks.put(digitalWallet.getWalletId(), new Object());
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
}
