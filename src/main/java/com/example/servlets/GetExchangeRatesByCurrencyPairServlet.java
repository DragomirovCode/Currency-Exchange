package com.example.servlets;

import com.example.dto.CurrencyDTO;
import com.example.dto.ExchangeRateDTO;
import com.example.services.CurrencyService;
import com.example.services.ExchangeRateService;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetExchangeRatesByCurrencyPairServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private CurrencyService currencyService;

    @Override
    public void init() {
        // Здесь вы можете инициализировать ваш сервис, например:
        exchangeRateService = new ExchangeRateService();
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String currencyPair = pathInfo.substring(1);
        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3);

        CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
        CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        int baseCurrencyId = baseCurrency.getId();
        int targetCurrencyId = targetCurrency.getId();

        ExchangeRateDTO exchangeRate = exchangeRateService.findByCurrencyPair(baseCurrencyId, targetCurrencyId);

        if (exchangeRate == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        Gson gson = new Gson();
        String json = gson.toJson(exchangeRate);
        resp.getWriter().write(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
