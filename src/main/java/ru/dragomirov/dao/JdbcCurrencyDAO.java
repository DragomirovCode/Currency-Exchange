package ru.dragomirov.dao;

import ru.dragomirov.entities.Currency;
import ru.dragomirov.utils.ConnectionUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcCurrencyDAO implements CurrencyDAO {
    private static final String FIND_ALL_QUERY = "SELECT * FROM Currency";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM Currency WHERE id=?";
    private static final String FIND_BY_CODE_QUERY = "SELECT * FROM Currency WHERE code=?";
    private static final String SAVE_QUERY = "INSERT INTO Currency (code, full_name, sign) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE Currency SET code=?, full_name=?, sign=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM Currency WHERE id=?";
    private final Connection connection;

    public JdbcCurrencyDAO() {
        try {
            this.connection = ConnectionUtils.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Currency mapResultSetToCurrency(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String fullName = resultSet.getString("full_name");
        String code = resultSet.getString("code");
        String sign = resultSet.getString("sign");

        Currency currency = new Currency(fullName,code, sign);
        currency.setId(id);
        return currency;
    }

    @Override
    public void save(Currency entity) {
        try (PreparedStatement statement = this.connection.prepareStatement(SAVE_QUERY)) {
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'save' (CurrencyDAO): " + e.getMessage());
        }
    }

    @Override
    public Optional<Currency> findById(Integer id) {
        Currency currency = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = mapResultSetToCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findById' (CurrencyDAO): " + e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(currency);
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
            System.err.println("Произошла ошибка при выполнении метода 'findAll' (CurrencyDAO): " + e.getMessage());
        }
        return currencies;
    }

    @Override
    public Optional<Currency> update(Currency entity) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_QUERY)) {
            statement.setString(1, entity.getCode());
            statement.setString(2, entity.getFullName());
            statement.setString(3, entity.getSign());
            statement.setInt(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'update' (CurrencyDAO): " + e.getMessage());
            return Optional.empty();
        }
        return Optional.of(entity);
    }

    @Override
    public void delete(Integer id) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'delete' (CurrencyDAO): " + e.getMessage());
        }
    }

    @Override
    public Optional<Currency> findByCode(String code) {
        Currency currency = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_CODE_QUERY)) {
            statement.setString(1, code);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    currency = mapResultSetToCurrency(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByCode' (CurrencyDAO): " + e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(currency);
    }
}
