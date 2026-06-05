package org.digitalwallet.model;

import org.digitalwallet.enums.TransactionType;

public class TransactionDetails {
    public TransactionDetails(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    TransactionType transactionType;
}
