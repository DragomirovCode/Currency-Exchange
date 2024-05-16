package ru.dragomirov.dao;

import ru.dragomirov.models.Currency;
import ru.dragomirov.models.ExchangeRate;
import ru.dragomirov.repositories.ExchangeRateRepository;
import ru.dragomirov.services.CurrencyService;
import ru.dragomirov.utils.ConnectionUtils;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcExchangeRateDAO implements ExchangeRateRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ExchangeRates";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ExchangeRates WHERE ID=?";
    private static final String FIND_BY_CURRENCY_PAIR_QUERY = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId=? AND TargetCurrencyId=?";
    private static final String SAVE_QUERY = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE ExchangeRates SET BaseCurrencyId=?, TargetCurrencyId=?, Rate=? WHERE ID=?";
    private static final String DELETE_QUERY = "DELETE FROM ExchangeRates WHERE ID=?";
    private final CurrencyService currencyService;
    private final Connection connection;

    public JdbcExchangeRateDAO() {
        try {
            this.connection = ConnectionUtils.getConnection();
            this.currencyService = new CurrencyService();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ExchangeRate mapResultSetToExchangeRate(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("ID");
        int baseCurrencyId = resultSet.getInt("BaseCurrencyId");
        int targetCurrencyId = resultSet.getInt("TargetCurrencyId");
        BigDecimal rate = resultSet.getBigDecimal("Rate");

        Currency baseCurrency = currencyService.findById(baseCurrencyId);
        Currency targetCurrency = currencyService.findById(targetCurrencyId);

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, rate);
        exchangeRate.setId(id);
        return exchangeRate;
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
            System.err.println("Произошла ошибка при выполнении метода 'findAll' (JdbcExchangeRateDAO): " + e.getMessage());
        }
        return exchangeRates;
    }


    @Override
    public ExchangeRate findById(int id) {
        ExchangeRate exchangeRate = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findById' (JdbcExchangeRateDAO): " + e.getMessage());
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRate findByCurrencyPair(int baseCurrencyId, int targetCurrencyId) {
        ExchangeRate exchangeRate = null;
        try (PreparedStatement statement = this.connection.prepareStatement(FIND_BY_CURRENCY_PAIR_QUERY)) {
            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByCurrencyPair' (JdbcExchangeRateDAO): " + e.getMessage());
        }
        return exchangeRate;
    }

    @Override
    public void save(ExchangeRate exchangeRate) {
        try (PreparedStatement statement = this.connection.prepareStatement(SAVE_QUERY)) {
            statement.setInt(1, exchangeRate.getBaseCurrency().getId());
            statement.setInt(2, exchangeRate.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'save' (JdbcExchangeRateDAO): " + e.getMessage());
        }
    }

    @Override
    public void update(ExchangeRate exchangeRate) {
        try (PreparedStatement statement = this.connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, exchangeRate.getBaseCurrency().getId());
            statement.setInt(2, exchangeRate.getTargetCurrency().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.setInt(4, exchangeRate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'update' (JdbcExchangeRateDAO): " + e.getMessage());
        }
    }

    @Override
    public void delete(ExchangeRate exchangeRate) {
        try (PreparedStatement statement = this.connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, exchangeRate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'delete' (JdbcExchangeRateDAO): " + e.getMessage());
        }
    }
}
