package ru.dragomirov.services;

import ru.dragomirov.models.Currency;
import ru.dragomirov.dao.CurrencyDAO;

import java.sql.Connection;
import java.util.List;

public class CurrencyService {
    private final CurrencyDAO currencyDAO;

    public CurrencyService() {
        this.currencyDAO = new CurrencyDAO();
    }

    public List<Currency> findAll() {
        return currencyDAO.findAll();
    }

    public Currency findById(int id, Connection connection) {
        return currencyDAO.findById(id, connection);
    }

    public Currency findByCode(String code) {
        return currencyDAO.findByCode(code);
    }

    public void save(Currency currency) {
        currencyDAO.save(currency);
    }

    public void update(Currency currency) {
        currencyDAO.update(currency);
    }

    public void delete(Currency currency) {
        currencyDAO.delete(currency);
    }
}
