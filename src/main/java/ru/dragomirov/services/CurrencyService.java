package ru.dragomirov.services;

import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.dao.CurrencyDAO;

import java.sql.Connection;
import java.util.List;

public class CurrencyService {
    private final CurrencyDAO currencyDAO;

    public CurrencyService() {
        this.currencyDAO = new CurrencyDAO();
    }

    public List<CurrencyDTO> findAll() {
        return currencyDAO.findAll();
    }

    public CurrencyDTO findById(int id, Connection connection) {
        return currencyDAO.findById(id, connection);
    }

    public CurrencyDTO findByCode(String code) {
        return currencyDAO.findByCode(code);
    }

    public void save(CurrencyDTO currency) {
        currencyDAO.save(currency);
    }

    public void update(CurrencyDTO currency) {
        currencyDAO.update(currency);
    }

    public void delete(CurrencyDTO currency) {
        currencyDAO.delete(currency);
    }
}
