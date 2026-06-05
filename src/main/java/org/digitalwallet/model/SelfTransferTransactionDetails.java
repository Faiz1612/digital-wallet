package org.digitalwallet.model;

import org.digitalwallet.enums.TransactionType;

public class SelfTransferTransactionDetails extends TransactionDetails {
    PaymentMethodDescription paymentMethodDescription;
    String userWalletId;

    public SelfTransferTransactionDetails(TransactionType transactionType) {
        super(transactionType);
    }

    public SelfTransferTransactionDetails(TransactionType transactionType, PaymentMethodDescription paymentMethodDescription, String userWalletId) {
        super(transactionType);
        this.paymentMethodDescription = paymentMethodDescription;
        this.userWalletId = userWalletId;
    }
}
