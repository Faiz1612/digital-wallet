package org.digitalwallet.exception;

public class WalletTransferReceiverException extends RuntimeException {
    public WalletTransferReceiverException(String message) {
        super(message);
    }

    public WalletTransferReceiverException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
