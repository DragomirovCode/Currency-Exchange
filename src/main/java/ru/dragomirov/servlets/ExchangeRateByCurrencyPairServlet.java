package ru.dragomirov.servlets;

import jakarta.servlet.ServletException;
import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dao.JdbcExchangeRateDAO;
import ru.dragomirov.dto.ExchangeRateDTO;
import ru.dragomirov.entities.Currency;
import ru.dragomirov.entities.ExchangeRate;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.utils.BigDecimalUtils;
import ru.dragomirov.utils.MappingUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @doGet: Получение конкретного обменного курса.
 * @doPatch: Обновление существующего в базе обменного курса.
 */
@WebServlet(name = "ExchangeRateByCurrencyPairServlet", urlPatterns = "/exchangeRate/*")
public class ExchangeRateByCurrencyPairServlet extends HttpErrorHandlingServlet {
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
                handleError(400, resp, "Коды валют пары отсутствуют в адресе");
                return;
            }

            String currencyPair = pathInfo.substring(1);
            String baseCurrencyCode = currencyPair.substring(0, 3);
            String targetCurrencyCode = currencyPair.substring(3);

            Optional<Currency> baseCurrency = jdbcCurrencyDAO.findByCode(baseCurrencyCode);
            Optional<Currency> targetCurrency = jdbcCurrencyDAO.findByCode(targetCurrencyCode);

            if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
                handleError(404, resp, "Обменный курс для пары не найден");
                return;
            }

            int baseCurrencyId = baseCurrency.get().getId();
            int targetCurrencyId = targetCurrency.get().getId();

            Optional<ExchangeRate> exchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(baseCurrencyId, targetCurrencyId);

            if (exchangeRate.isEmpty()) {
                handleError(404, resp, "Обменный курс для пары не найден");
                return;
            }

            ExchangeRateDTO exchangeRateDTO = MappingUtils.exchangeRateToDTO(exchangeRate.get());

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRateDTO);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
            handleError(500, resp, "База данных недоступна");
        }
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();

            String currencyPair = pathInfo.substring(1);
            String baseCurrencyCode = currencyPair.substring(0, 3);
            String targetCurrencyCode = currencyPair.substring(3);

            String parameter = req.getReader().readLine();

            String rateString = parameter.replace("rate=", "");

            if (rateString.isEmpty()) {
                handleError(400, resp, "Отсутствует нужное поле формы");
                return;
            }

            BigDecimal rate = BigDecimalUtils.parseBigDecimal(rateString);

            Optional<Currency> baseCurrency = jdbcCurrencyDAO.findByCode(baseCurrencyCode);
            Optional<Currency> targetCurrency = jdbcCurrencyDAO.findByCode(targetCurrencyCode);

            if (baseCurrency.isEmpty() || targetCurrency.isEmpty()){
                handleError(404, resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            int baseCurrencyId = baseCurrency.get().getId();
            int targetCurrencyId = targetCurrency.get().getId();

            Optional<ExchangeRate> existingExchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(baseCurrencyId, targetCurrencyId);
            if (existingExchangeRate.isEmpty()) {
                handleError(404, resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            existingExchangeRate.get().setRate(rate);
            jdbcExchangeRateDAO.update(existingExchangeRate.get());

            ExchangeRateDTO exchangeRateDTO = MappingUtils.exchangeRateToDTO(existingExchangeRate.get());

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRateDTO);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
            handleError(500, resp, "База данных недоступна");
        }
    }
}
