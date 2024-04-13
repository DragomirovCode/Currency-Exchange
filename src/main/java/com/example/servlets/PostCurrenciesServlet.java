package com.example.servlets;

import com.example.dto.CurrencyDTO;
import com.example.services.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "PostCurrenciesServlet", urlPatterns = "/currencies")
public class PostCurrenciesServlet extends HttpServlet {
    private CurrencyService currencyService;

    @Override
    public void init() {
        // Здесь вы можете инициализировать ваш сервис, например:
        currencyService = new CurrencyService();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();

        String name = req.getParameter("name");
        String code = req.getParameter("code");
        String sign = req.getParameter("sign");

        if (name == null || code == null || sign == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CurrencyDTO existingCurrency = currencyService.findByCode(code);
        if (existingCurrency != null) {
            resp.setStatus(HttpServletResponse.SC_CONFLICT);
            return;
        }

        CurrencyDTO newCurrency = new CurrencyDTO(name, code, sign);
        currencyService.save(newCurrency);

        String json = gson.toJson(newCurrency);
        resp.getWriter().write(json);
        resp.setStatus(HttpServletResponse.SC_CREATED);
    }
}
