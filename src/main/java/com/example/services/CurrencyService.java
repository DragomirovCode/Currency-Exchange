package com.example.services;

import com.example.dto.CurrencyDTO;
import com.example.dao.CurrencyDAO;

import java.util.List;

public class CurrencyService {
    private final CurrencyDAO currencyDAO;

    public CurrencyService() {
        this.currencyDAO = new CurrencyDAO();
    }

    public List<CurrencyDTO> findAll() {
        return currencyDAO.findAll();
    }

    public CurrencyDTO findById(int id) {
        return currencyDAO.findById(id);
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
