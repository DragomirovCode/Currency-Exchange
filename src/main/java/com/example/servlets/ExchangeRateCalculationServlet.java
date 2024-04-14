package com.example.servlets;

import com.example.dto.CalculationDTO;
import com.example.dto.CurrencyDTO;
import com.example.dto.ExchangeRateDTO;
import com.example.services.CurrencyService;
import com.example.services.ExchangeRateService;
import com.example.util.BaseServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * doGet: Расчёт перевода определённого количества средств из одной валюты в другую.
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
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String fromCurrencyCode = req.getParameter("from");
            String toCurrencyCode = req.getParameter("to");
            String amountString = req.getParameter("amount");

            if (fromCurrencyCode == null || toCurrencyCode == null || amountString == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            BigDecimal amount;
            try {
                amount = new BigDecimal(amountString);
            } catch (NumberFormatException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyDTO fromCurrency = currencyService.findByCode(fromCurrencyCode);
            CurrencyDTO toCurrency = currencyService.findByCode(toCurrencyCode);

            if (fromCurrency == null || toCurrency == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            int fromCurrencyId = fromCurrency.getId();
            int toCurrencyId = toCurrency.getId();

            ExchangeRateDTO exchangeRate = exchangeRateService.findByCurrencyPair(fromCurrencyId, toCurrencyId);

            if (exchangeRate == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());

            CurrencyDTO baseCurrency = exchangeRate.getBaseCurrencyId();
            CurrencyDTO targetCurrency = exchangeRate.getTargetCurrencyId();

            CalculationDTO calculationDTO = new CalculationDTO(baseCurrency, targetCurrency, exchangeRate.getRate(),
                    amount, convertedAmount);

            String jsonResponse = new Gson().toJson(calculationDTO);

            resp.getWriter().write(jsonResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e, "База данных недоступна");
        }
    }
}
