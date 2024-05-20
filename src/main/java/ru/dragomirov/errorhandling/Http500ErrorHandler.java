package ru.dragomirov.errorhandling;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Http500ErrorHandler implements ErrorHandler{
    @Override
    public void httpErrors(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
}
