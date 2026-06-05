package org.digitalwallet.strategy;

import org.digitalwallet.model.PaymentMethodDescription;

public class NetBankingPaymentMethod implements PaymentMethodStrategy{
    @Override
    public boolean execute(PaymentMethodDescription paymentMethodDescription) {
        // call external api to execute this
        return false;
    }
}
