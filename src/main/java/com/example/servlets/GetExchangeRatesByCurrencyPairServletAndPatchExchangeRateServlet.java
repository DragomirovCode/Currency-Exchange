package com.example.servlets;

import com.example.dto.CurrencyDTO;
import com.example.dto.ExchangeRateDTO;
import com.example.services.CurrencyService;
import com.example.services.ExchangeRateService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "GetExchangeRatesByCurrencyPairServletAndPatchExchangeRateServlet", urlPatterns = "/exchangeRate/*")
public class GetExchangeRatesByCurrencyPairServletAndPatchExchangeRateServlet extends HttpServlet {
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Получаем параметры из запроса
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.isEmpty() || pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Извлекаем информацию о валютной паре из URL
        String currencyPair = pathInfo.substring(1);
        String baseCurrencyCode = currencyPair.substring(0, 3);
        String targetCurrencyCode = currencyPair.substring(3);

        // Получаем информацию о курсе обмена из тела запроса
        String rateString = req.getParameter("rate");
        if (rateString == null) {
            // Если отсутствует поле rate, возвращаем ошибку
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Парсим курс обмена
        double rate;
        try {
            rate = Double.parseDouble(rateString);
        } catch (NumberFormatException e) {
            // Если не удается распарсить курс обмена, возвращаем ошибку
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Получаем информацию о валютах из базы данных
        CurrencyDTO baseCurrency = currencyService.findByCode(baseCurrencyCode);
        CurrencyDTO targetCurrency = currencyService.findByCode(targetCurrencyCode);

        if (baseCurrency == null || targetCurrency == null) {
            // Если валютная пара отсутствует в базе данных, возвращаем ошибку
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Обновляем курс обмена в базе данных
        int baseCurrencyId = baseCurrency.getId();
        int targetCurrencyId = targetCurrency.getId();

        ExchangeRateDTO existingExchangeRate = exchangeRateService.findByCurrencyPair(baseCurrencyId, targetCurrencyId);
        if (existingExchangeRate == null) {
            // Если курс обмена не найден, возвращаем ошибку
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Обновляем курс обмена
        existingExchangeRate.setRate(BigDecimal.valueOf(rate));
        exchangeRateService.update(existingExchangeRate);

        // Формируем ответ с обновленными данными
        Gson gson = new Gson();
        String json = gson.toJson(existingExchangeRate);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.getWriter().write(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
