package org.digitalwallet.model;

import org.digitalwallet.enums.PaymentMethod;

public class PaymentMethodDescription {
    public PaymentMethodDescription(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    PaymentMethod paymentMethod;
}
