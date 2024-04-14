package com.example.servlets;

import com.example.services.CurrencyService;
import com.example.util.BaseServletUtils;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "GetAllCurrenciesServlet", urlPatterns = "/currencies")
public class GetAllCurrenciesServlet extends BaseServletUtils {
    private CurrencyService currencyService;

    @Override
    public void init() {
        currencyService = new CurrencyService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        try {
            String json = gson.toJson(currencyService.findAll());
            resp.getWriter().write(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            handleException(resp, e, "База данных недоступна");
        }
    }
}
