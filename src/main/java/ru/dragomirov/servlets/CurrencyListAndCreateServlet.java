package ru.dragomirov.servlets;

import ru.dragomirov.dao.JdbcCurrencyDAO;
import ru.dragomirov.models.Currency;
import ru.dragomirov.commons.BaseServlet;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/**
 * @doGet: Получение списка валют.
 * @doPost: Добавление новой валюты в базу.
 */
@WebServlet(name = "CurrencyListAndCreateServlet", urlPatterns = "/currencies")
public class CurrencyListAndCreateServlet extends BaseServlet {
    private JdbcCurrencyDAO jdbcCurrencyDAO;

    @Override
    public void init() {
        this.jdbcCurrencyDAO = new JdbcCurrencyDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(jdbcCurrencyDAO.findAll());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");

            if (name == null || code == null || sign == null) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            Optional<Currency> existingCurrency = jdbcCurrencyDAO.findByCode(code);
            if (existingCurrency.isPresent()) {
                http409Errors(resp, "Валюта с таким кодом уже существует");
                return;
            }

            Currency newCurrency = new Currency(name, code, sign);
            jdbcCurrencyDAO.save(newCurrency);

            Gson gson = new Gson();
            String json = gson.toJson(newCurrency);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
