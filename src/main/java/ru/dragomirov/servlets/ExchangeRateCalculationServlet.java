package ru.dragomirov.servlets;

import ru.dragomirov.dto.CalculationDTO;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.service.ExchangeRateCalculationService;

import java.io.IOException;

/**
 * @doGet: Расчёт перевода определённого количества средств из одной валюты в другую.
 */
@WebServlet(name = "ExchangeRateCalculationServlet", urlPatterns = "/exchange")
public class ExchangeRateCalculationServlet extends HttpErrorHandlingServlet {
    private ExchangeRateCalculationService exchangeRateCalculationService;
    @Override
    public void init() {
        this.exchangeRateCalculationService = new ExchangeRateCalculationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String fromCurrencyCode = req.getParameter("from");
            String toCurrencyCode = req.getParameter("to");
            String amountString = req.getParameter("amount");

            if (fromCurrencyCode.isEmpty() || toCurrencyCode.isEmpty() || amountString.isEmpty()) {
                handleError(400, resp, "Отсутствует нужное поле формы");
                return;
            }

            CalculationDTO calculationDTO = exchangeRateCalculationService.calculateExchangeRate(fromCurrencyCode, toCurrencyCode, amountString);

            if (calculationDTO == null){
                handleError(404, resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            String jsonResponse = new Gson().toJson(calculationDTO);
            resp.getWriter().write(jsonResponse);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
            handleError(500, resp, "База данных недоступна");
        }
    }
}
