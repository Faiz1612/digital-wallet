package org.digitalwallet.model;

import org.digitalwallet.enums.PaymentMethod;

public class CreditCardPayment extends PaymentMethodDescription{
    PaymentMethod paymentMethod;
    String bankName;
    String cardNumber;

    public CreditCardPayment(PaymentMethod paymentMethod, PaymentMethod paymentMethod1, String bankName, String cardNumber) {
        super(paymentMethod);
        this.paymentMethod = paymentMethod1;
        this.bankName = bankName;
        this.cardNumber = cardNumber;
    }
}
