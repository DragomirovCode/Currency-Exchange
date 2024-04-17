package ru.dragomirov.repositories;

import ru.dragomirov.models.ExchangeRate;

import java.util.List;

public interface ExchangeRateRepository {
    List<ExchangeRate> findAll();
    ExchangeRate findById(int id);
    ExchangeRate findByCurrencyPair(int baseCurrencyCode, int targetCurrencyCode);
    void save(ExchangeRate exchangeRate);
    void update(ExchangeRate exchangeRate);
    void delete(ExchangeRate exchangeRate);
}
