package org.digitalwallet.model;

import org.digitalwallet.enums.PaymentMethod;

public class NetBankingPayment extends PaymentMethodDescription{
    PaymentMethod paymentMethod;
    String bankName;
    String accountNumber;

    public NetBankingPayment(PaymentMethod paymentMethod, PaymentMethod paymentMethod1, String bankName, String accountNumber) {
        super(paymentMethod);
        this.paymentMethod = paymentMethod1;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }
}
