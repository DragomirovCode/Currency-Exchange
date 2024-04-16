package ru.dragomirov.dao;

import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.dto.ExchangeRateDTO;
import ru.dragomirov.repositories.ExchangeRateRepository;
import ru.dragomirov.services.CurrencyService;
import ru.dragomirov.util.ConnectionDB;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ExchangeRateDAO implements ExchangeRateRepository {
    private static final String FIND_ALL_QUERY = "SELECT * FROM ExchangeRates";
    private static final String FIND_BY_ID_QUERY = "SELECT * FROM ExchangeRates WHERE id=?";
    private static final String FIND_BY_CURRENCY_PAIR_QUERY = "SELECT * FROM ExchangeRates WHERE baseCurrencyId=? AND targetCurrencyId=?";
    private static final String SAVE_QUERY = "INSERT INTO ExchangeRates (baseCurrencyId, targetCurrencyId, rate) VALUES (?, ?, ?)";
    private static final String UPDATE_QUERY = "UPDATE ExchangeRates SET baseCurrencyId=?, targetCurrencyId=?, rate=? WHERE id=?";
    private static final String DELETE_QUERY = "DELETE FROM ExchangeRates WHERE id=?";
    private final CurrencyService currencyService;

    public ExchangeRateDAO() {
        this.currencyService = new CurrencyService();
    }

    private ExchangeRateDTO mapResultSetToExchangeRate(ResultSet resultSet, Connection connection) throws SQLException {
        int id = resultSet.getInt("id");
        int baseCurrencyId = resultSet.getInt("baseCurrencyId");
        int targetCurrencyId = resultSet.getInt("targetCurrencyId");
        BigDecimal rate = resultSet.getBigDecimal("rate");

        CurrencyDTO baseCurrency = currencyService.findById(baseCurrencyId, connection);
        CurrencyDTO targetCurrency = currencyService.findById(targetCurrencyId, connection);

        ExchangeRateDTO exchangeRate = new ExchangeRateDTO(baseCurrency, targetCurrency, rate);
        exchangeRate.setId(id);
        return exchangeRate;
    }

    @Override
    public List<ExchangeRateDTO> findAll() {
        List<ExchangeRateDTO> exchangeRates = new ArrayList<>();
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(FIND_ALL_QUERY);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                ExchangeRateDTO exchangeRate = mapResultSetToExchangeRate(resultSet, connection);
                exchangeRates.add(exchangeRate);
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findAll': " + e.getMessage());
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
                    exchangeRate = mapResultSetToExchangeRate(resultSet, connection);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findById': " + e.getMessage());
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
                    exchangeRate = mapResultSetToExchangeRate(resultSet, connection);
                }
            }
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'findByCurrencyPair': " + e.getMessage());
        }
        return exchangeRate;
    }

    @Override
    public void save(ExchangeRateDTO exchangeRate) {
        try (Connection connection = ConnectionDB.getConnection();
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
    public void update(ExchangeRateDTO exchangeRate) {
        try (Connection connection = ConnectionDB.getConnection();
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
    public void delete(ExchangeRateDTO exchangeRate) {
        try (Connection connection = ConnectionDB.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_QUERY)) {
            statement.setInt(1, exchangeRate.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Произошла ошибка при выполнении метода 'delete': " + e.getMessage());
        }
    }
}
