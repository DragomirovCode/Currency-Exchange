package ru.dragomirov.utils;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;

public abstract class BaseServletUtils extends HttpServlet {
    protected void http400Errors(HttpServletResponse resp, String errorMessage) throws IOException{
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
    protected void http404Errors(HttpServletResponse resp, String errorMessage) throws IOException{
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
    protected void http409Errors(HttpServletResponse resp, String errorMessage) throws IOException{
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
    protected void http500Errors(HttpServletResponse resp, Exception e, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
        e.printStackTrace();
    }
    protected BigDecimal parseBigDecimal(String string) {
        BigDecimal rate = null;
        try {
            rate = new BigDecimal(string);
        } catch (NumberFormatException e) {
            System.err.println("Произошла ошибка при выполнении метода 'parseBigDecimal': " + e.getMessage());
        }
        return rate;
    }
}
