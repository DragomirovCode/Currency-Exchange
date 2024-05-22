package ru.dragomirov.utils;

import java.util.Currency;
import java.util.Set;

/**
 * CurrencyUtils предоставляет метод для поиска валюты по ее коду и символу.
 */
public class CurrencyUtils {
    private final Set<Currency> currencySet = Currency.getAvailableCurrencies();
    public boolean searchCurrency(String code, String symbol) {
        for(Currency currency: currencySet) {
            if (currency.getCurrencyCode().equals(code) && currency.getSymbol().equals(symbol)) {
                return true;
            }
        }
        return false;
    }
}
