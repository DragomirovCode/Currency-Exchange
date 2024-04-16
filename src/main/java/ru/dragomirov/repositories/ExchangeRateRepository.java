package ru.dragomirov.repositories;

import ru.dragomirov.dto.ExchangeRateDTO;

import java.util.List;

public interface ExchangeRateRepository {
    List<ExchangeRateDTO> findAll();
    ExchangeRateDTO findById(int id);
    ExchangeRateDTO findByCurrencyPair(int baseCurrencyCode, int targetCurrencyCode);
    void save(ExchangeRateDTO exchangeRate);
    void update(ExchangeRateDTO exchangeRate);
    void delete(ExchangeRateDTO exchangeRate);
}