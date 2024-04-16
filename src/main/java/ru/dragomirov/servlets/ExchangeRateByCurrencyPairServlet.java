package ru.dragomirov.servlets;

import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.dto.ExchangeRateDTO;
import ru.dragomirov.services.CurrencyService;
import ru.dragomirov.services.ExchangeRateService;
import ru.dragomirov.utils.BaseServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @doGet: Получение конкретного обменного курса.
 * @doPost: Обновление существующего в базе обменного курса.
 */
@WebServlet(name = "ExchangeRateByCurrencyPairServlet", urlPatterns = "/exchangeRate/*")
public class ExchangeRateByCurrencyPairServlet extends BaseServletUtils {
    private ExchangeRateService exchangeRateService;
    private CurrencyService currencyService;

    @Override
    public void init() {
        exchangeRateService = new ExchangeRateService();
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
                http400Errors(resp, "Коды валют пары отсутствуют в адресе");
                return;
            }

            String currencyPair = pathInfo.substring(1);
            String baseCurrencyCode = currencyPair.substring(0, 3);
            String targetCurrencyCode = currencyPair.substring(3);

            CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
            CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);

            if (baseCurrency == null || targetCurrency == null) {
                http404Errors(resp, "Обменный курс для пары не найден");
                return;
            }

            int baseCurrencyId = baseCurrency.getId();
            int targetCurrencyId = targetCurrency.getId();

            ExchangeRateDTO exchangeRate = exchangeRateService.findByCurrencyPair(baseCurrencyId, targetCurrencyId);

            if (exchangeRate == null) {
                http404Errors(resp, "Обменный курс для пары не найден");
                return;
            }

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRate);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String pathInfo = req.getPathInfo();
            if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            String currencyPair = pathInfo.substring(1);
            String baseCurrencyCode = currencyPair.substring(0, 3);
            String targetCurrencyCode = currencyPair.substring(3);

            String rateString = req.getParameter("rate");
            if (rateString == null) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            BigDecimal rate = parseBigDecimal(rateString);

            CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
            CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);

            int baseCurrencyId = baseCurrency.getId();
            int targetCurrencyId = targetCurrency.getId();

            ExchangeRateDTO existingExchangeRate = exchangeRateService.findByCurrencyPair(baseCurrencyId, targetCurrencyId);
            if (existingExchangeRate == null) {
                http404Errors(resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            existingExchangeRate.setRate(rate);
            exchangeRateService.update(existingExchangeRate);

            Gson gson = new Gson();
            String json = gson.toJson(existingExchangeRate);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
