package org.digitalwallet.repository;

import org.digitalwallet.model.DigitalWallet;

import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

public interface WalletDb {
    DigitalWallet createWallet(DigitalWallet digitalWallet);
    Optional<DigitalWallet> getWallet(String userId);
    boolean isWalletExist(String userId);
    Object getWalletLock(String walletId);
    ReentrantLock getReentrantWalletLock(String walletId);
    void updateWallet(DigitalWallet wallet, String userId);
}
