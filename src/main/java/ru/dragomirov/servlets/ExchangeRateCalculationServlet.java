package ru.dragomirov.servlets;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dao.JdbcExchangeRateDAO;
import ru.dragomirov.models.Calculation;
import ru.dragomirov.models.Currency;
import ru.dragomirov.models.ExchangeRate;
import ru.dragomirov.commons.BaseServlet;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @doGet: Расчёт перевода определённого количества средств из одной валюты в другую.
 */
@WebServlet(name = "ExchangeRateCalculationServlet", urlPatterns = "/exchange")
public class ExchangeRateCalculationServlet extends BaseServlet {
    private JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private JdbcCurrencyDAO jdbcCurrencyDAO;

    @Override
    public void init() {
        this.jdbcExchangeRateDAO = new JdbcExchangeRateDAO();
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String fromCurrencyCode = req.getParameter("from");
            String toCurrencyCode = req.getParameter("to");
            String amountString = req.getParameter("amount");

            if (fromCurrencyCode == null || toCurrencyCode == null || amountString == null) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            BigDecimal amount = parseBigDecimal(amountString);

            Optional<Currency> fromCurrency = jdbcCurrencyDAO.findByCode(fromCurrencyCode);
            Optional<Currency> toCurrency = jdbcCurrencyDAO.findByCode(toCurrencyCode);

            if (fromCurrency.isEmpty() || toCurrency.isEmpty()) {
                http404Errors(resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            int fromCurrencyId = fromCurrency.get().getId();
            int toCurrencyId = toCurrency.get().getId();

            Optional<ExchangeRate> exchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(fromCurrencyId, toCurrencyId);

            if (exchangeRate.isEmpty()) {
                http404Errors(resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            BigDecimal convertedAmount = amount.multiply(exchangeRate.get().getRate());

            Currency baseCurrency = exchangeRate.get().getBaseCurrency();
            Currency targetCurrency = exchangeRate.get().getTargetCurrency();

            Calculation calculation = new Calculation(baseCurrency, targetCurrency, exchangeRate.get().getRate(),
                    amount, convertedAmount);

            String jsonResponse = new Gson().toJson(calculation);

            resp.getWriter().write(jsonResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
