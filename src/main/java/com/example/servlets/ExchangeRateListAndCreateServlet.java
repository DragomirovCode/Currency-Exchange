package com.example.servlets;

import com.example.dto.CurrencyDTO;
import com.example.dto.ExchangeRateDTO;
import com.example.services.CurrencyService;
import com.example.services.ExchangeRateService;
import com.example.util.BaseServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;

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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Gson gson = new Gson();
            String json = gson.toJson(exchangeRateService.findAll());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rateString = req.getParameter("rate");

            if (baseCurrencyCode == null || targetCurrencyCode == null || rateString == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            BigDecimal rate;
            try {
                rate = new BigDecimal(rateString);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
            CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);

            if (baseCurrency == null || targetCurrency == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            ExchangeRateDTO existingExchangeRate = exchangeRateService.findByCurrencyPair(baseCurrency.getId(),
                    targetCurrency.getId());
            if (existingExchangeRate != null) {
                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

            ExchangeRateDTO exchangeRate = new ExchangeRateDTO(baseCurrency, targetCurrency, rate);
            exchangeRateService.save(exchangeRate);

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRate);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            handleException(resp, e, "База данных недоступна");
        }
    }
}
