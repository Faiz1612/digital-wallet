package org.digitalwallet.exception;

public class TransactionCreationException extends RuntimeException {
    public TransactionCreationException(String message) {
        super(message);
    }
}
