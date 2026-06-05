package org.digitalwallet.model;

import org.digitalwallet.enums.TransactionType;

public class WalletTransferTransactionDetails extends TransactionDetails {
    String senderWalletId;

    public WalletTransferTransactionDetails(TransactionType transactionType, String senderWalletId, String receiverWalletId) {
        super(transactionType);
        this.senderWalletId = senderWalletId;
        this.receiverWalletId = receiverWalletId;
    }

    String receiverWalletId;

    public WalletTransferTransactionDetails(TransactionType transactionType) {
        super(transactionType);
    }
}
