package ru.dragomirov.dao;

import ru.dragomirov.models.ExchangeRate;

import java.util.Optional;

public interface ExchangeRateDAO extends CrudDAO<ExchangeRate, Integer> {
    Optional<ExchangeRate> findByCurrencyPair(int baseCurrencyCode, int targetCurrencyCode);
}
