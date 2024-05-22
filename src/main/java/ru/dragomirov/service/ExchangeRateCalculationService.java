package ru.dragomirov.service;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dao.JdbcExchangeRateDAO;
import ru.dragomirov.dto.CalculationDTO;
import ru.dragomirov.dto.CalculationDTOFactory;
import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.entities.Currency;
import ru.dragomirov.entities.ExchangeRate;
import ru.dragomirov.utils.BigDecimalUtils;
import ru.dragomirov.utils.MappingUtils;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * ExchangeRateCalculationService реализует бизнес-логику ExchangeRateCalculationServlet
 */
public class ExchangeRateCalculationService {
    private static final int NUM_DECIMAL_PLACES = 6;
    private JdbcCurrencyDAO jdbcCurrencyDAO;
    private JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private CalculationDTOFactory calculationDTOFactory;

    public ExchangeRateCalculationService() {
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
        this.jdbcExchangeRateDAO = new JdbcExchangeRateDAO();
        this.calculationDTOFactory = new CalculationDTOFactory();
    }

    public CalculationDTO calculateExchangeRate(String fromCurrencyCode, String toCurrencyCode, String amountString) {
        BigDecimal amount = parseAmount(amountString);
        Currency fromCurrency = findCurrency(fromCurrencyCode);
        Currency toCurrency = findCurrency(toCurrencyCode);
        if (fromCurrency == null || toCurrency == null){
            return null;
        } else {
            ExchangeRate exchangeRate = findExchangeRate(fromCurrency, toCurrency);
            BigDecimal convertedAmount = calculateConvertedAmount(amount, exchangeRate);
            return createCalculationDTO(amount, convertedAmount, exchangeRate);
        }
    }

    private BigDecimal parseAmount(String amountString) {
        return BigDecimalUtils.parseBigDecimal(amountString);
    }

    private Currency findCurrency(String currencyCode) {
        Optional<Currency> currency = jdbcCurrencyDAO.findByCode(currencyCode);
        return currency.orElse(null);
    }

    private ExchangeRate findExchangeRate(Currency fromCurrency, Currency toCurrency) {
        Optional<ExchangeRate> exchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(fromCurrency.getId(), toCurrency.getId());
        if (exchangeRate.isPresent()) {
            return exchangeRate.get();
        } else {
           return currencyRateBA(fromCurrency, toCurrency);
        }
    }

    private ExchangeRate currencyRateBA(Currency fromCurrency, Currency toCurrency) {
        Optional<ExchangeRate> reverseExchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(toCurrency.getId(), fromCurrency.getId());
        if (reverseExchangeRate.isPresent()) {
            BigDecimal reverseRate = BigDecimal.ONE.divide(reverseExchangeRate.get().getRate(), NUM_DECIMAL_PLACES, BigDecimal.ROUND_HALF_UP);
            return new ExchangeRate(fromCurrency, toCurrency, reverseRate);
        }
        return null;
    }

    private BigDecimal calculateConvertedAmount(BigDecimal amount, ExchangeRate exchangeRate) {
        return amount.multiply(exchangeRate.getRate());
    }

    private CalculationDTO createCalculationDTO(BigDecimal amount, BigDecimal convertedAmount, ExchangeRate exchangeRate) {
        CurrencyDTO baseCurrency = MappingUtils.currencyToDTO(exchangeRate.getBaseCurrency());
        CurrencyDTO targetCurrency = MappingUtils.currencyToDTO(exchangeRate.getTargetCurrency());
        return calculationDTOFactory.createCalculationDTO(
                baseCurrency, targetCurrency, exchangeRate.getRate(),
                amount, convertedAmount);
    }
}
