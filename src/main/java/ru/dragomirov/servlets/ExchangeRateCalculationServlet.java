package ru.dragomirov.servlets;

import ru.dragomirov.models.Calculation;
import ru.dragomirov.models.Currency;
import ru.dragomirov.models.ExchangeRate;
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
 * @doGet: Расчёт перевода определённого количества средств из одной валюты в другую.
 */
@WebServlet(name = "ExchangeRateCalculationServlet", urlPatterns = "/exchange")
public class ExchangeRateCalculationServlet extends BaseServletUtils {
    private ExchangeRateService exchangeRateService;
    private CurrencyService currencyService;

    @Override
    public void init() {
        exchangeRateService = new ExchangeRateService();
        currencyService = new CurrencyService();
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

            Currency fromCurrency = currencyService.findByCode(fromCurrencyCode);
            Currency toCurrency = currencyService.findByCode(toCurrencyCode);

            if (fromCurrency == null || toCurrency == null) {
                http404Errors(resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            int fromCurrencyId = fromCurrency.getId();
            int toCurrencyId = toCurrency.getId();

            ExchangeRate exchangeRate = exchangeRateService.findByCurrencyPair(fromCurrencyId, toCurrencyId);

            if (exchangeRate == null) {
                http404Errors(resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());

            Currency baseCurrency = exchangeRate.getBaseCurrencyId();
            Currency targetCurrency = exchangeRate.getTargetCurrencyId();

            Calculation calculation = new Calculation(baseCurrency, targetCurrency, exchangeRate.getRate(),
                    amount, convertedAmount);

            String jsonResponse = new Gson().toJson(calculation);

            resp.getWriter().write(jsonResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
