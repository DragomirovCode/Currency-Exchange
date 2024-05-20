package ru.dragomirov.servlets;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dao.JdbcExchangeRateDAO;
import ru.dragomirov.dto.ExchangeRateDTO;
import ru.dragomirov.entities.Currency;
import ru.dragomirov.entities.ExchangeRate;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import ru.dragomirov.utils.MappingUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @doGet: Получение списка всех обменных курсов.
 * @doPost: Добавление нового обменного курса в базу.
 */
@WebServlet(name = "ExchangeRateListAndCreateServlet", urlPatterns = "/exchangeRates")
public class ExchangeRateListAndCreateServlet extends HttpErrorHandlingServlet {
    private JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private JdbcCurrencyDAO jdbcCurrencyDAO;

    @Override
    public void init() {
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
        this.jdbcExchangeRateDAO = new JdbcExchangeRateDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Gson gson = new Gson();
            List<ExchangeRate> exchangeRateList = jdbcExchangeRateDAO.findAll();
            List<ExchangeRateDTO> exchangeRateDTOList = exchangeRateList.stream()
                    .map(MappingUtils:: exchangeRateToDTO).collect(Collectors.toList());

            String json = gson.toJson(exchangeRateDTOList);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
            handleError(500, resp, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String baseCurrencyCode = req.getParameter("baseCurrencyCode");
            String targetCurrencyCode = req.getParameter("targetCurrencyCode");
            String rateString = req.getParameter("rate");

            if (baseCurrencyCode.isEmpty() || targetCurrencyCode.isEmpty() || rateString.isEmpty()) {
                handleError(400, resp, "Отсутствует нужное поле формы");
                return;
            }

            BigDecimal rate;
            try {
                rate = new BigDecimal(rateString);
            } catch (NumberFormatException e) {
                handleError(400, resp, "Не правильный формат");
                return;
            }

            Optional<Currency> baseCurrency = jdbcCurrencyDAO.findByCode(baseCurrencyCode);
            Optional<Currency> targetCurrency = jdbcCurrencyDAO.findByCode(targetCurrencyCode);

            if (baseCurrency.isEmpty() || targetCurrency.isEmpty()) {
                handleError(404, resp, "Одна (или обе) валюта из валютной пары не существует в БД");
                return;
            }

            Optional<ExchangeRate> existingExchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(baseCurrency.get().getId(),
                    targetCurrency.get().getId());
            if (existingExchangeRate.isPresent()) {
                handleError(409, resp, "Валютная пара с таким кодом уже существует");
                return;
            }

            ExchangeRate exchangeRate = new ExchangeRate(baseCurrency.get(), targetCurrency.get(), rate);
            jdbcExchangeRateDAO.save(exchangeRate);

            ExchangeRateDTO exchangeRateDTO = MappingUtils.exchangeRateToDTO(exchangeRate);

            Gson gson = new Gson();
            String json = gson.toJson(exchangeRateDTO);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            System.err.println("Произошла ошибка: " + e.getMessage());
            e.printStackTrace();
            handleError(500, resp, "База данных недоступна");
        }
    }
}
