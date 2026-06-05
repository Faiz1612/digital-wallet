package org.digitalwallet.strategy;

import org.digitalwallet.enums.PaymentMethod;

import java.util.HashMap;
import java.util.Map;

public class PaymentStrategyFactory {
    private Map<PaymentMethod, PaymentMethodStrategy> paymentMethodStrategyMap;

    public PaymentStrategyFactory() {
        this.paymentMethodStrategyMap = new HashMap<>();
        this.paymentMethodStrategyMap.put(PaymentMethod.CREDIT_CARD, new CreditCardPaymentMethod());
        this.paymentMethodStrategyMap.put(PaymentMethod.NET_BANKING, new NetBankingPaymentMethod());
    }

    public PaymentMethodStrategy getPaymentMethod(PaymentMethod paymentMethod) {
        return this.paymentMethodStrategyMap.get(paymentMethod);
    }
}
