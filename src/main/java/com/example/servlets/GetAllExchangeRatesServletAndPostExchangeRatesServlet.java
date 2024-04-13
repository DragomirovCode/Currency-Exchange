package com.example.servlets;

import com.example.dto.CurrencyDTO;
import com.example.dto.ExchangeRateDTO;
import com.example.services.CurrencyService;
import com.example.services.ExchangeRateService;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "GetAllExchangeRatesServletAndPostExchangeRatesServlet", urlPatterns = "/exchangeRates")
public class GetAllExchangeRatesServletAndPostExchangeRatesServlet extends HttpServlet {
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

        Gson gson = new Gson();
        String json = gson.toJson(exchangeRateService.findAll());
        resp.getWriter().write(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();

        String baseCurrencyCode = req.getParameter("baseCurrencyCode");
        String targetCurrencyCode = req.getParameter("targetCurrencyCode");
        String rateString = req.getParameter("rate");

        // Проверка наличия всех полей формы
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

        // Поиск валют в базе данных
        CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
        CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);

        // Проверка наличия обеих валют в базе данных
        if (baseCurrency == null || targetCurrency == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Создание нового объекта ExchangeRateDTO и сохранение его в базу данных
        ExchangeRateDTO exchangeRate = new ExchangeRateDTO(baseCurrency, targetCurrency, rate);
        exchangeRateService.save(exchangeRate);

        // Отправка ответа клиенту
        String json = gson.toJson(exchangeRate);
        resp.getWriter().write(json);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
