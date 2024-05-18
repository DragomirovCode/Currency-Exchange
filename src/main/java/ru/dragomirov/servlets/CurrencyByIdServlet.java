package ru.dragomirov.servlets;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.models.Currency;
import ru.dragomirov.commons.BaseServlet;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Optional;

/**
 * @doGet: Получение конкретной валюты.
 */
@WebServlet(name = "CurrencyByIdServlet", urlPatterns = "/currency/*")
public class CurrencyByIdServlet extends BaseServlet {
    private JdbcCurrencyDAO jdbcCurrencyDAO;

    @Override
    public void init() {
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String pathInfo = req.getPathInfo();
            String currencyCode = pathInfo.substring(1);

            if (currencyCode.isEmpty()) {
                http400Errors(resp,"Код валюты отсутствует в адресе");
                return;
            }

            Optional<Currency> currency = jdbcCurrencyDAO.findByCode(currencyCode);

            if (currency.isEmpty()) {
                http404Errors(resp, "Валюта не найдена");
                return;
            }

            Gson gson = new Gson();
            String json = gson.toJson(currency.get());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e){
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
