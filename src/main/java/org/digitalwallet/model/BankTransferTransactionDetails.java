package org.digitalwallet.model;

import org.digitalwallet.enums.TransactionType;

public class BankTransferTransactionDetails extends TransactionDetails {
    String senderWalletId;
    String receiverAccountId;

    public BankTransferTransactionDetails(TransactionType transactionType) {
        super(transactionType);
    }

    public BankTransferTransactionDetails(TransactionType transactionType, String senderWalletId, String receiverAccountId) {
        super(transactionType);
        this.senderWalletId = senderWalletId;
        this.receiverAccountId = receiverAccountId;
    }
}
