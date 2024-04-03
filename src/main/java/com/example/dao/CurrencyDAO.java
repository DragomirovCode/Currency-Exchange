package com.example.dao;

import com.example.dto.CurrencyDTO;
import com.example.repositories.CurrencyRepository;
import com.example.util.ConnectionDB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CurrencyDAO implements CurrencyRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM Currency";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM Currency WHERE id=?";
    private static final String FIND_BY_CODE_QUERY = "SELECT * FROM Currency WHERE code=?";
    private static final String SAVE_QUERY = "INSERT INTO Currency (code, fullName, sign) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Currency SET code=?, fullName=?, sign=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM Currency WHERE id=?";

    private CurrencyDTO mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String fullName = resultSet.getString("fullName");
        String code = resultSet.getString("code");
        String sign = resultSet.getString("sign");

        CurrencyDTO currency = new CurrencyDTO(fullName,code, sign);
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
            statement.setInt(4, currency.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(CurrencyDTO currency) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, currency.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}