package org.digitalwallet.dao;

import org.digitalwallet.model.Transaction;

public class UserTransactionDetails {
    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public boolean isDebited() {
        return debited;
    }

    public void setDebited(boolean debited) {
        this.debited = debited;
    }

    Transaction transaction;

    public UserTransactionDetails(Transaction transaction) {
        this.transaction = transaction;
        this.debited = true;
    }

    boolean debited;
}
