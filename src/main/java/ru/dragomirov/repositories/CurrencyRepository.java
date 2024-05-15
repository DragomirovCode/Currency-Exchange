package ru.dragomirov.repositories;

import ru.dragomirov.models.Currency;

import java.sql.Connection;
import java.util.List;

public interface CurrencyRepository {
    List<Currency> findAll();
    Currency findById(int id);
    Currency findByCode(String code);
    void save(Currency currency);
    void update(Currency currency);
    void delete(Currency currency);
}
