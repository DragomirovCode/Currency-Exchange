package ru.dragomirov.servlets;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dao.JdbcExchangeRateDAO;
import ru.dragomirov.models.Currency;
import ru.dragomirov.models.ExchangeRate;
import ru.dragomirov.commons.BaseServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @doGet: Получение списка всех обменных курсов.
 * @doPost: Добавление нового обменного курса в базу.
 */
@WebServlet(name = "ExchangeRateListAndCreateServlet", urlPatterns = "/exchangeRates")
public class ExchangeRateListAndCreateServlet extends BaseServlet {
    private JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private JdbcCurrencyDAO jdbcCurrencyDAO;

    @Override
    public void init() {
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
        this.jdbcExchangeRateDAO = new JdbcExchangeRateDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(jdbcExchangeRateDAO.findAll());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rateString = req.getParameter("rate");

            if (baseCurrencyCode == null || targetCurrencyCode == null || rateString == null) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            BigDecimal rate;
            try {
                rate = new BigDecimal(rateString);
            } catch (NumberFormatException e) {
                http400Errors(resp, "Не правильный формат");
                return;
            }

            Optional<Currency> baseCurrency = jdbcCurrencyDAO.findByCode(baseCurrencyCode);
            Optional<Currency> targetCurrency = jdbcCurrencyDAO.findByCode(targetCurrencyCode);

            if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
                http404Errors(resp, "Одна (или обе) валюта из валютной пары не существует в БД");
                return;
            }

            Optional<ExchangeRate> existingExchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(baseCurrency.get().getId(),
                    targetCurrency.get().getId());
            if (existingExchangeRate.isPresent()) {
                http409Errors(resp, "Валютная пара с таким кодом уже существует");
                return;
            }

            ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(), targetCurrency.get(), rate);
            jdbcExchangeRateDAO.save(exchangeRate);

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRate);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
