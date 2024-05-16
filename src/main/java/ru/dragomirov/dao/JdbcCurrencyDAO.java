package ru.dragomirov.dao;

import ru.dragomirov.models.Currency;
import ru.dragomirov.repositories.CurrencyRepository;
import ru.dragomirov.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcCurrencyDAO implements CurrencyRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM Currency";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM Currency WHERE ID=?";
    private static final String FIND_BY_CODE_QUERY = "SELECT * FROM Currency WHERE Code=?";
    private static final String SAVE_QUERY = "INSERT INTO Currency (Code, FullName, Sign) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Currency SET Code=?, FullName=?, Sign=? WHERE ID=?";
    private static final String DELETE_QUERY = "DELETE FROM Currency WHERE ID=?";
    private final Connection connection;

    public JdbcCurrencyDAO() {
        try {
            this.connection = ConnectionUtils.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        String fullName = resultSet.getString("FullName");
        String code = resultSet.getString("Code");
        String sign = resultSet.getString("Sign");

        Currency currency = new Currency(fullName,code, sign);
        currency.setId(id);
        return currency;
    }

    @Override
    public List<Currency> findAll() {
        List<Currency> currencies = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                Currency currency = mapResultSetToCurrency(resultSet);
                currencies.add(currency);
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findAll' (JdbcCurrencyDAO): " + e.getMessage());
        }
        return currencies;
    }

    @Override
    public Currency findById(int id) {
        Currency currency = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = mapResultSetToCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findById' (JdbcCurrencyDAO): " + e.getMessage());
        }
        return currency;
    }

    @Override
    public Currency findByCode(String code) {
        Currency currency = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_CODE_QUERY)) {
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = mapResultSetToCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByCode' (JdbcCurrencyDAO): " + e.getMessage());
        }
        return currency;
    }

    @Override
    public void save(Currency currency) {
        try (PreparedStatement statement = this.connection.prepareStatement(SAVE_QUERY)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'save' (JdbcCurrencyDAO): " + e.getMessage());
        }
    }

    @Override
    public void update(Currency currency) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, currency.getCode());
            statement.setString(2, currency.getFullName());
            statement.setString(3, currency.getSign());
            statement.setInt(4, currency.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'update' (JdbcCurrencyDAO): " + e.getMessage());
        }
    }

    @Override
    public void delete(Currency currency) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, currency.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'delete' (JdbcCurrencyDAO): " + e.getMessage());
        }
    }
}
