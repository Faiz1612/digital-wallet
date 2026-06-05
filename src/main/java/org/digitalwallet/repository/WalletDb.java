package org.digitalwallet.repository;

import org.digitalwallet.model.DigitalWallet;

import java.util.Optional;

public interface WalletDb {
    DigitalWallet createWallet(DigitalWallet digitalWallet);
    Optional<DigitalWallet> getWallet(String userId);
    boolean isWalletExist(String userId);
    Object getWalletLock(String walletId);
}
