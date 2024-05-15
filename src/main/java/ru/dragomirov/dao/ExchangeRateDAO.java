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

public class ExchangeRateDAO implements ExchangeRateRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ExchangeRates";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ExchangeRates WHERE ID=?";
    private static final String FIND_BY_CURRENCY_PAIR_QUERY = "SELECT * FROM ExchangeRates WHERE BaseCurrencyId=? AND TargetCurrencyId=?";
    private static final String SAVE_QUERY = "INSERT INTO ExchangeRates (BaseCurrencyId, TargetCurrencyId, Rate) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE ExchangeRates SET BaseCurrencyId=?, TargetCurrencyId=?, Rate=? WHERE ID=?";
    private static final String DELETE_QUERY = "DELETE FROM ExchangeRates WHERE ID=?";
    private final CurrencyService currencyService;

    public ExchangeRateDAO() {
        this.currencyService = new CurrencyService();
    }

    private ExchangeRate mapResultSetToExchangeRate(ResultSet resultSet, Connection connection) throws SQLException {
        int id = resultSet.getInt("ID");
        int baseCurrencyId = resultSet.getInt("BaseCurrencyId");
        int targetCurrencyId = resultSet.getInt("TargetCurrencyId");
        BigDecimal rate = resultSet.getBigDecimal("Rate");

        Currency baseCurrency = currencyService.findById(baseCurrencyId, connection);
        Currency targetCurrency = currencyService.findById(targetCurrencyId, connection);

        ExchangeRate exchangeRate = new ExchangeRate(baseCurrency, targetCurrency, rate);
        exchangeRate.setId(id);
        return exchangeRate;
    }

    @Override
    public List<ExchangeRate> findAll() {
        List<ExchangeRate> exchangeRates = new ArrayList<>();
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ExchangeRate exchangeRate = mapResultSetToExchangeRate(resultSet, connection);
                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findAll': " + e.getMessage());
        }
        return exchangeRates;
    }


    @Override
    public ExchangeRate findById(int id) {
        ExchangeRate exchangeRate = null;
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_ID_QUERY)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet, connection);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findById': " + e.getMessage());
        }
        return exchangeRate;
    }

    @Override
    public ExchangeRate findByCurrencyPair(int baseCurrencyId, int targetCurrencyId) {
        ExchangeRate exchangeRate = null;
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_BY_CURRENCY_PAIR_QUERY)) {
            statement.setInt(1, baseCurrencyId);
            statement.setInt(2, targetCurrencyId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    exchangeRate = mapResultSetToExchangeRate(resultSet, connection);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByCurrencyPair': " + e.getMessage());
        }
        return exchangeRate;
    }

    @Override
    public void save(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(SAVE_QUERY)) {
            statement.setInt(1, exchangeRate.getBaseCurrencyId().getId());
            statement.setInt(2, exchangeRate.getTargetCurrencyId().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'save': " + e.getMessage());
        }
    }

    @Override
    public void update(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_QUERY)) {
            statement.setInt(1, exchangeRate.getBaseCurrencyId().getId());
            statement.setInt(2, exchangeRate.getTargetCurrencyId().getId());
            statement.setBigDecimal(3, exchangeRate.getRate());
            statement.setInt(4, exchangeRate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'update': " + e.getMessage());
        }
    }

    @Override
    public void delete(ExchangeRate exchangeRate) {
        try (Connection connection = ConnectionUtils.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, exchangeRate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'delete': " + e.getMessage());
        }
    }
}
