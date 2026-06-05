package org.digitalwallet.strategy;

import org.digitalwallet.model.PaymentMethodDescription;

public interface PaymentMethodStrategy {
    boolean execute(PaymentMethodDescription paymentMethodDescription);
}
