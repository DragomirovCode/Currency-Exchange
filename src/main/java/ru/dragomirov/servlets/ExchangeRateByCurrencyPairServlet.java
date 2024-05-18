package ru.dragomirov.servlets;

import jakarta.servlet.ServletException;
import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dao.JdbcExchangeRateDAO;
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
 * @doGet: Получение конкретного обменного курса.
 * @doPatch: Обновление существующего в базе обменного курса.
 */
@WebServlet(name = "ExchangeRateByCurrencyPairServlet", urlPatterns = "/exchangeRate/*")
public class ExchangeRateByCurrencyPairServlet extends BaseServlet {
    private JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private JdbcCurrencyDAO jdbcCurrencyDAO;

    @Override
    public void init() {
        this.jdbcExchangeRateDAO = new JdbcExchangeRateDAO();
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")){
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
                http400Errors(resp, "Коды валют пары отсутствуют в адресе");
                return;
            }

            String currencyPair = pathInfo.substring(1);
            String baseCurrencyCode = currencyPair.substring(0, 3);
            String targetCurrencyCode = currencyPair.substring(3);

            Optional<Currency> baseCurrency = jdbcCurrencyDAO.findByCode(baseCurrencyCode);
            Optional<Currency> targetCurrency = jdbcCurrencyDAO.findByCode(targetCurrencyCode);

            if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
                http404Errors(resp, "Обменный курс для пары не найден");
                return;
            }

            int baseCurrencyId = baseCurrency.get().getId();
            int targetCurrencyId = targetCurrency.get().getId();

            Optional<ExchangeRate> exchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(baseCurrencyId, targetCurrencyId);

            if (exchangeRate.isEmpty()) {
                http404Errors(resp, "Обменный курс для пары не найден");
                return;
            }

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRate.get());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            String currencyPair = pathInfo.substring(1);
            String baseCurrencyCode = currencyPair.substring(0, 3);
            String targetCurrencyCode = currencyPair.substring(3);

            String parameter = req.getReader().readLine();
            if (parameter == null) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            String rateString = parameter.replace("rate=", "");

            BigDecimal rate = parseBigDecimal(rateString);

            Optional<Currency> baseCurrency = jdbcCurrencyDAO.findByCode(baseCurrencyCode);
            Optional<Currency> targetCurrency = jdbcCurrencyDAO.findByCode(targetCurrencyCode);

            if (baseCurrency.isEmpty() || targetCurrency.isEmpty()){
                http404Errors(resp, "Одна из валют отсутствует в базе данных");
                return;
            }

            int baseCurrencyId = baseCurrency.get().getId();
            int targetCurrencyId = targetCurrency.get().getId();

            Optional<ExchangeRate> existingExchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(baseCurrencyId, targetCurrencyId);
            if (existingExchangeRate.isEmpty()) {
                http404Errors(resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            existingExchangeRate.get().setRate(rate);
            jdbcExchangeRateDAO.update(existingExchangeRate.get());

            Gson gson = new Gson();
            String json = gson.toJson(existingExchangeRate.get());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
