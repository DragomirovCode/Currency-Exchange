package com.example.servlets;

import com.example.dto.CalculationDTO;
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

@WebServlet(name = "GetExchangeCalculationServlet", urlPatterns = "/exchange")
public class GetExchangeCalculationServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private CurrencyService currencyService;

    @Override
    public void init() {
        // Инициализация сервисов обменных курсов и валют
        exchangeRateService = new ExchangeRateService();
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // Установка заголовков ответа для возврата JSON
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        // Извлечение параметров запроса
        String fromCurrencyCode = req.getParameter("from");
        String toCurrencyCode = req.getParameter("to");
        String amountString = req.getParameter("amount");

        // Проверка наличия всех параметров
        if (fromCurrencyCode == null || toCurrencyCode == null || amountString == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Парсинг суммы для обмена
        BigDecimal amount;
        try {
            amount = new BigDecimal(amountString);
        } catch (NumberFormatException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Поиск информации о валютах по их кодам
        CurrencyDTO fromCurrency = currencyService.findByCode(fromCurrencyCode);
        CurrencyDTO toCurrency = currencyService.findByCode(toCurrencyCode);

        // Проверка наличия обеих валют в базе данных
        if (fromCurrency == null || toCurrency == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Получение идентификаторов валют
        int fromCurrencyId = fromCurrency.getId();
        int toCurrencyId = toCurrency.getId();

        // Поиск обменного курса между валютами
        ExchangeRateDTO exchangeRate = exchangeRateService.findByCurrencyPair(fromCurrencyId, toCurrencyId);

        // Проверка наличия обменного курса
        if (exchangeRate == null) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Выполнение расчета конвертированной суммы
        BigDecimal convertedAmount = amount.multiply(exchangeRate.getRate());

        // Получение информации о базовой и целевой валютах
        CurrencyDTO baseCurrency = exchangeRate.getBaseCurrencyId();
        CurrencyDTO targetCurrency = exchangeRate.getTargetCurrencyId();

        // Формирование объекта CalculationDTO для ответа
        CalculationDTO calculationDTO = new CalculationDTO(baseCurrency, targetCurrency, exchangeRate.getRate(), amount, convertedAmount);

        // Преобразование объекта в JSON-строку с помощью Gson
        String jsonResponse = new Gson().toJson(calculationDTO);

        // Отправка ответа клиенту
        resp.getWriter().write(jsonResponse);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
