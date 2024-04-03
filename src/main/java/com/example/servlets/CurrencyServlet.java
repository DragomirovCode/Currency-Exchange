package com.example.servlets;

import com.example.services.CurrencyService;
import com.google.gson.Gson;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private final CurrencyService currencyService;

    public CurrencyServlet(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        Gson gson = new Gson();
        String json = gson.toJson(currencyService.findAll());
        resp.getWriter().write(json);
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
