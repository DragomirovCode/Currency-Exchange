package ru.dragomirov.dao;

import ru.dragomirov.entities.Currency;

import java.util.Optional;

public interface CurrencyDAO extends CrudDAO<Currency, Integer> {
    Optional<Currency> findByCode(String code);
}
