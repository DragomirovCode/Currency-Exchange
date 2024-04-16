package ru.dragomirov.dao;

import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.repositories.CurrencyRepository;
import ru.dragomirov.utils.ConnectionDB;

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

    private CurrencyDTO mapResultSetToCurrency(ResultSet resultSet, Connection connection) throws SQLException {
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
                CurrencyDTO currency = mapResultSetToCurrency(resultSet, connection);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findAll': " + e.getMessage());
        }
        return currencies;
    }

    @Override
    public CurrencyDTO findById(int id, Connection connection) {
        CurrencyDTO currency = null;
        try (PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = mapResultSetToCurrency(resultSet, connection);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findById': " + e.getMessage());
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
                    currency = mapResultSetToCurrency(resultSet, connection);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByCode': " + e.getMessage());
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
            System.err.println("Произошла ошибка при выполнении метода 'save': " + e.getMessage());
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
            System.err.println("Произошла ошибка при выполнении метода 'update': " + e.getMessage());
        }
    }

    @Override
    public void delete(CurrencyDTO currency) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, currency.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'delete': " + e.getMessage());
        }
    }
}
