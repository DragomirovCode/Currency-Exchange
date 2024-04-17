package ru.dragomirov.services;


import ru.dragomirov.dao.ExchangeRateDAO;
import ru.dragomirov.models.ExchangeRate;

import java.util.List;

public class ExchangeRateService  {
   private final ExchangeRateDAO exchangeRateDAO;

    public ExchangeRateService() {
        this.exchangeRateDAO = new ExchangeRateDAO();
    }

    public List<ExchangeRate> findAll(){
        return exchangeRateDAO.findAll();
    }

    public ExchangeRate findById(int id){
        return exchangeRateDAO.findById(id);
    }

    public ExchangeRate findByCurrencyPair(int baseCurrencyId, int targetCurrencyId){
        return exchangeRateDAO.findByCurrencyPair(baseCurrencyId, targetCurrencyId);
    }

    public void save(ExchangeRate exchangeRate){
        exchangeRateDAO.save(exchangeRate);
    }

    public void update(ExchangeRate exchangeRate){
        exchangeRateDAO.update(exchangeRate);
    }

    public void delete(ExchangeRate exchangeRate){
        exchangeRateDAO.delete(exchangeRate);
    }
}
