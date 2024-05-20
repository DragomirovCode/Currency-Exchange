package ru.dragomirov.exceptions;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Http400ErrorHandler implements ErrorHandler {
    @Override
    public void httpErrors(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
}
