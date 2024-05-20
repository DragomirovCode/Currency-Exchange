package ru.dragomirov.errorhandling;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class Http404ErrorHandler implements ErrorHandler{
    @Override
    public void httpErrors(HttpServletResponse resp, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
    }
}
