package com.example.services;

import com.example.DTO.CurrencyDTO;
import com.example.repositories.CurrencyRepository;
import com.example.util.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyService implements CurrencyRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM Currencies";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM Currencies WHERE id=?";
    private static final String FIND_BY_CODE_QUERY = "SELECT * FROM Currencies WHERE code=?";
    private static final String SAVE_QUERY = "INSERT INTO Currencies (code, fullName, sign) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Currencies SET code=?, fullName=?, sign=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM Currencies WHERE id=?";
    private CurrencyDTO mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String code = resultSet.getString("code");
        String fullName = resultSet.getString("fullName");
        String sign = resultSet.getString("sign");

        CurrencyDTO currency = new CurrencyDTO(code, fullName, sign);
        currency.setId(id);
        return currency;
    }

    @Override
    public List<CurrencyDTO> findAll() {
        List<CurrencyDTO> currencies = new ArrayList<>();
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                CurrencyDTO currency = mapResultSetToCurrency(resultSet);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }

    @Override
    public CurrencyDTO findById(int id) {
        CurrencyDTO currency = null;
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = mapResultSetToCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }

    @Override
    public CurrencyDTO findByCode(String code) {
        CurrencyDTO currency = null;
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CODE_QUERY)) {
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = mapResultSetToCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currency;
    }

    @Override
    public void save(CurrencyDTO currency) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(CurrencyDTO currency) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.setInt(4, currency.getId()); // Устанавливаем ID валюты для обновления
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(CurrencyDTO currency) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, currency.getId()); // Устанавливаем ID валюты для удаления
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
