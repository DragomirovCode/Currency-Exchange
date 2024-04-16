package ru.dragomirov.servlets;

import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.dto.ExchangeRateDTO;
import ru.dragomirov.services.CurrencyService;
import ru.dragomirov.services.ExchangeRateService;
import ru.dragomirov.utils.BaseServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @doGet: Получение списка всех обменных курсов.
 * @doPost: Добавление нового обменного курса в базу.
 */
@WebServlet(name = "ExchangeRateListAndCreateServlet", urlPatterns = "/exchangeRates")
public class ExchangeRateListAndCreateServlet extends BaseServletUtils {
    private ExchangeRateService exchangeRateService;
    private CurrencyService currencyService;

    @Override
    public void init() {
        exchangeRateService = new ExchangeRateService();
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setResponseHeaders(resp);

        try {
            Gson gson = new Gson();
            String json = gson.toJson(exchangeRateService.findAll());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setResponseHeaders(resp);

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

            CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
            CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);

            if (baseCurrency == null || targetCurrency == null) {
                http404Errors(resp, "Одна (или обе) валюта из валютной пары не существует в БД");
                return;
            }

            ExchangeRateDTO existingExchangeRate = exchangeRateService.findByCurrencyPair(baseCurrency.getId(),
                    targetCurrency.getId());
            if (existingExchangeRate != null) {
                http409Errors(resp, "Валютная пара с таким кодом уже существует");
                return;
            }

            ExchangeRateDTO exchangeRate = new ExchangeRateDTO(baseCurrency, targetCurrency, rate);
            exchangeRateService.save(exchangeRate);

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRate);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
