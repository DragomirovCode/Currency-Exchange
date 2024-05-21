package ru.dragomirov.dao;

import ru.dragomirov.entities.Currency;
import ru.dragomirov.entities.ExchangeRate;
import ru.dragomirov.utils.ConnectionUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class JdbcExchangeRateDAO implements ExchangeRateDAO {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ExchangeRates";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ExchangeRates WHERE id=?";
    private static final String FIND_BY_CURRENCY_PAIR_QUERY = "SELECT * FROM ExchangeRates WHERE base_currency_id=? AND target_currency_id=?";
    private static final String FIND_BY_BASE_CURRENCY_QUERY = "SELECT * FROM ExchangeRates WHERE base_currency_id=?";
    private static final String FIND_BY_TARGET_CURRENCY_QUERY = "SELECT * FROM ExchangeRates WHERE target_currency_id=?";
    private static final String SAVE_QUERY = "INSERT INTO ExchangeRates (base_currency_id, target_currency_id, rate) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE ExchangeRates SET base_currency_id=?, target_currency_id=?, rate=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM ExchangeRates WHERE id=?";
    private final JdbcCurrencyDAO jdbcCurrencyDAO;
    private final Connection connection;

    public JdbcExchangeRateDAO() {
        try {
            this.connection = ConnectionUtils.getConnection();
            this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate mapResultSetToExchangeRate(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        int baseCurrencyId = resultSet.getInt("base_currency_id");
        int targetCurrencyId = resultSet.getInt("target_currency_id");
        BigDecimal rate = resultSet.getBigDecimal("rate");

        Optional<Currency> baseCurrencyOpt = jdbcCurrencyDAO.findById(baseCurrencyId);
        Optional<Currency> targetCurrencyOpt = jdbcCurrencyDAO.findById(targetCurrencyId);

        if (baseCurrencyOpt.isPresent() && targetCurrencyOpt.isPresent()) {
            Currency baseCurrency = baseCurrencyOpt.get();
            Currency targetCurrency = targetCurrencyOpt.get();
            ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, rate);
            exchangeRate.setId(id);
            return exchangeRate;
        } else {
            throw new SQLException("Валюта не найдена");
        }
    }

    @Override
    public void save(ExchangeRate entity) {
        try (PreparedStatement statement = this.connection.prepareStatement(SAVE_QUERY)) {
            statement.setInt(1, entity.getBaseCurrency().getId());
            statement.setInt(2, entity.getTargetCurrency().getId());
            statement.setBigDecimal(3, entity.getRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'save' (ExchangeRateDAO): " + e.getMessage());
        }
    }

    @Override
    public Optional<ExchangeRate> findById(Integer id) {
        ExchangeRate exchangeRate = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findById' (ExchangeRateDAO): " + e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(exchangeRate);
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ExchangeRate exchangeRate = mapResultSetToExchangeRate(resultSet);
                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findAll' (ExchangeRateDAO): " + e.getMessage());
        }
        return exchangeRates;
    }

    @Override
    public Optional<ExchangeRate> update(ExchangeRate entity) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, entity.getBaseCurrency().getId());
            statement.setInt(2, entity.getTargetCurrency().getId());
            statement.setBigDecimal(3, entity.getRate());
            statement.setInt(4, entity.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'update' (ExchangeRateDAO): " + e.getMessage());
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
            System.err.println("Произошла ошибка при выполнении метода 'delete' (ExchangeRateDAO): " + e.getMessage());
        }
    }

    @Override
    public Optional<ExchangeRate> findByCurrencyPair(int baseCurrencyCode, int targetCurrencyCode) {
        ExchangeRate exchangeRate = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_CURRENCY_PAIR_QUERY)) {
            statement.setInt(1, baseCurrencyCode);
            statement.setInt(2, targetCurrencyCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByCurrencyPair' (ExchangeRateDAO): " + e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(exchangeRate);
    }

    @Override
    public Optional<ExchangeRate> findByBaseCurrency(int baseCurrencyCode) {
        ExchangeRate exchangeRate = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_BASE_CURRENCY_QUERY)) {
            statement.setInt(1, baseCurrencyCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByBaseCurrency' (ExchangeRateDAO): " + e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(exchangeRate);
    }

    @Override
    public Optional<ExchangeRate> findByTargetCurrency(int targetCurrencyCode) {
        ExchangeRate exchangeRate = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_TARGET_CURRENCY_QUERY)) {
            statement.setInt(1, targetCurrencyCode);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByTargetCurrency' (ExchangeRateDAO): " + e.getMessage());
            return Optional.empty();
        }
        return Optional.ofNullable(exchangeRate);
    }
}
