package com.example.services;

import com.example.DTO.ExchangeRateDTO;
import com.example.repositories.ExchangeRateRepository;
import com.example.util.ConnectionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateService implements ExchangeRateRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ExchangeRates";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ExchangeRates WHERE ID=?";
    private static final String FIND_BY_CURRENCY_PAIR_QUERY = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId=? AND TargetCurrencyId=?";
    private static final String SAVE_QUERY = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE ExchangeRates SET BaseCurrencyId=?, TargetCurrencyId=?, Rate=? WHERE ID=?";
    private static final String DELETE_QUERY = "DELETE FROM ExchangeRates WHERE ID=?";

    private ExchangeRateDTO mapResultSetToExchangeRate(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        int baseCurrencyId = resultSet.getInt("BaseCurrencyId");
        int targetCurrencyId = resultSet.getInt("TargetCurrencyId");
        BigDecimal rate = resultSet.getBigDecimal("Rate");

        ExchangeRateDTO exchangeRate = new ExchangeRateDTO(baseCurrencyId, targetCurrencyId, rate);
        exchangeRate.setID(id);
        return exchangeRate;
    }

    @Override
    public List<ExchangeRateDTO> findAll() {
        List<ExchangeRateDTO> exchangeRates = new ArrayList<>();
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ExchangeRateDTO exchangeRate = mapResultSetToExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRates;
    }

    @Override
    public ExchangeRateDTO findById(int id) {
        ExchangeRateDTO exchangeRate = null;
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRateDTO findByCurrencyPair(int baseCurrencyId, int targetCurrencyId) {
        ExchangeRateDTO exchangeRate = null;
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CURRENCY_PAIR_QUERY)) {
            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return exchangeRate;
    }

    @Override
    public void save(ExchangeRateDTO exchangeRate) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {
            statement.setInt(1, exchangeRate.getBaseCurrencyId());
            statement.setInt(2, exchangeRate.getTargetCurrencyId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(ExchangeRateDTO exchangeRate) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, exchangeRate.getBaseCurrencyId());
            statement.setInt(2, exchangeRate.getTargetCurrencyId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.setInt(4, exchangeRate.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(ExchangeRateDTO exchangeRate) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, exchangeRate.getID());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
