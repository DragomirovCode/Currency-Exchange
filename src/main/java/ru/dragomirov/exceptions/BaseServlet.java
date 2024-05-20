package ru.dragomirov.exceptions;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public abstract class BaseServlet extends HttpServlet {
    private Map<Integer, ErrorHandler> errorHandlers = new HashMap();

    {
        errorHandlers.put(400, new Http400ErrorHandler());
        errorHandlers.put(404, new Http404ErrorHandler());
        errorHandlers.put(409, new Http409ErrorHandler());
        errorHandlers.put(500, new Http500ErrorHandler());
    }

    protected void handleError(int errorCode, HttpServletResponse resp, String errorMessage) throws IOException {
        ErrorHandler errorHandler = errorHandlers.get(errorCode);
        if (errorHandler != null){
            errorHandler.httpErrors(resp, errorMessage);
        } else {
            System.err.println("Обработчик ошибок не найден для кода ошибки: " + errorCode);
        }
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
