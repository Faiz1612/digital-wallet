package org.digitalwallet.service;

import org.digitalwallet.enums.Currency;

public class CurrencyConverterService {
    public double convertMoney(double amount, Currency currentCurrency, Currency expectedCurrency) {
        if(currentCurrency.equals(expectedCurrency)) {
            return amount;
        }

        // get exchange rate, specifically what is 1 currentCurrency equals to expectedCurrency.
        // Assuming i get this value from external API
        double exchangeRate = 90.0; // taking hardcode value but usuallt comes from external api
        return (amount*exchangeRate);
    }
}
