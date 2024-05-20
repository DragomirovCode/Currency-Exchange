package ru.dragomirov.servlets;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.dao.JdbcExchangeRateDAO;
import ru.dragomirov.dto.CalculationDTO;
import ru.dragomirov.dto.CalculationDTOFactory;
import ru.dragomirov.dto.CurrencyDTO;
import ru.dragomirov.entities.Currency;
import ru.dragomirov.entities.ExchangeRate;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.dragomirov.utils.BigDecimalUtils;
import ru.dragomirov.utils.MappingUtils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

/**
 * @doGet: Расчёт перевода определённого количества средств из одной валюты в другую.
 */
@WebServlet(name = "ExchangeRateCalculationServlet", urlPatterns = "/exchange")
public class ExchangeRateCalculationServlet extends HttpErrorHandlingServlet {
    private JdbcExchangeRateDAO jdbcExchangeRateDAO;
    private JdbcCurrencyDAO jdbcCurrencyDAO;
    private CalculationDTOFactory calculationDTOFactory;

    @Override
    public void init() {
        this.jdbcExchangeRateDAO = new JdbcExchangeRateDAO();
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
        this.calculationDTOFactory = new CalculationDTOFactory();
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

            BigDecimal amount = BigDecimalUtils.parseBigDecimal(amountString);

            Optional<Currency> fromCurrency = jdbcCurrencyDAO.findByCode(fromCurrencyCode);
            Optional<Currency> toCurrency = jdbcCurrencyDAO.findByCode(toCurrencyCode);

            if (fromCurrency.isEmpty() || toCurrency.isEmpty()) {
                handleError(404, resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            int fromCurrencyId = fromCurrency.get().getId();
            int toCurrencyId = toCurrency.get().getId();

            Optional<ExchangeRate> exchangeRate = jdbcExchangeRateDAO.findByCurrencyPair(fromCurrencyId, toCurrencyId);

            if (exchangeRate.isEmpty()) {
                handleError(404, resp, "Валютная пара отсутствует в базе данных");
                return;
            }

            BigDecimal convertedAmount = amount.multiply(exchangeRate.get().getRate());

            CurrencyDTO baseCurrency = MappingUtils.currencyToDTO(exchangeRate.get().getBaseCurrency());
            CurrencyDTO targetCurrency = MappingUtils.currencyToDTO(exchangeRate.get().getTargetCurrency());

            CalculationDTO calculationDTO = calculationDTOFactory.createCalculationDTO(
                    baseCurrency, targetCurrency, exchangeRate.get().getRate(),
                    amount, convertedAmount);

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
