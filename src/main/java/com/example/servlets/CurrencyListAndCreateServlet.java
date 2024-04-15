package com.example.servlets;

import com.example.dto.CurrencyDTO;
import com.example.services.CurrencyService;
import com.example.util.BaseServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @doGet: Получение списка валют.
 * @doPost: Добавление новой валюты в базу.
 */
@WebServlet(name = "CurrencyListAndCreateServlet", urlPatterns = "/currencies")
public class CurrencyListAndCreateServlet extends BaseServletUtils {
    private CurrencyService currencyService;

    @Override
    public void init() {
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            Gson gson = new Gson();
            String json = gson.toJson(currencyService.findAll());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        try {
            String name = req.getParameter("name");
            String code = req.getParameter("code");
            String sign = req.getParameter("sign");

            if (name == null || code == null || sign == null) {
                http400Errors(resp, "Отсутствует нужное поле формы");
                return;
            }

            CurrencyDTO existingCurrency = currencyService.findByCode(code);
            if (existingCurrency != null) {
                http409Errors(resp, "Валюта с таким кодом уже существует");
                return;
            }

            CurrencyDTO newCurrency = new CurrencyDTO(name, code, sign);
            currencyService.save(newCurrency);

            Gson gson = new Gson();
            String json = gson.toJson(newCurrency);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        } catch (Exception e) {
            http500Errors(resp, e, "База данных недоступна");
        }
    }
}
