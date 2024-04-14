package com.example.servlets;

import com.example.dto.CurrencyDTO;
import com.example.services.CurrencyService;
import com.example.util.BaseServletUtils;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import java.io.IOException;

/**
 * @doGet: Получение конкретной валюты.
 */
@WebServlet(name = "CurrencyByIdServlet", urlPatterns = "/currency/*")
public class CurrencyByIdServlet extends BaseServletUtils {
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
            String pathInfo = req.getPathInfo();
            String currencyCode = pathInfo.substring(1);

            if (currencyCode.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CurrencyDTO currency = currencyService.findByCode(currencyCode);

            if (currency == null) {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            Gson gson = new Gson();
            String json = gson.toJson(currency);
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e){
            handleException(resp, e, "База данных недоступна");
        }
    }
}
