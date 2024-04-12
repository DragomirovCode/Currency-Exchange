package com.example.repositories;

import com.example.dto.CurrencyDTO;

import java.sql.Connection;
import java.util.List;

public interface CurrencyRepository {
    List<CurrencyDTO> findAll();
    CurrencyDTO findById(int id, Connection connection);
    CurrencyDTO findByCode(String code);
    void save(CurrencyDTO currency);
    void update(CurrencyDTO currency);
    void delete(CurrencyDTO currency);
}
