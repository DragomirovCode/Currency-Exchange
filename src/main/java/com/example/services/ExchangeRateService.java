package com.example.services;


import com.example.DAO.ExchangeRateDAO;
import com.example.DTO.ExchangeRateDTO;

import java.util.List;

public class ExchangeRateService  {
   private final ExchangeRateDAO exchangeRateDAO;

    public ExchangeRateService(ExchangeRateDAO exchangeRateDAO) {
        this.exchangeRateDAO = exchangeRateDAO;
    }

    public List<ExchangeRateDTO> findAll(){
        return exchangeRateDAO.findAll();
    }

    public ExchangeRateDTO findById(int id){
        return exchangeRateDAO.findById(id);
    }

    public ExchangeRateDTO findByCurrencyPair(int baseCurrencyId, int targetCurrencyId){
        return exchangeRateDAO.findByCurrencyPair(baseCurrencyId, targetCurrencyId);
    }

    public void save(ExchangeRateDTO exchangeRate){
        exchangeRateDAO.save(exchangeRate);
    }

    public void update(ExchangeRateDTO exchangeRate){
        exchangeRateDAO.update(exchangeRate);
    }

    public void delete(ExchangeRateDTO exchangeRate){
        exchangeRateDAO.delete(exchangeRate);
    }
}
