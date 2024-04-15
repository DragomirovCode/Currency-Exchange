package com.example.util;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class BaseServletUtils extends HttpServlet {
    protected void handleException(HttpServletResponse resp, Exception e, String errorMessage) throws IOException {
        resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        String errorResponse = "{\"message\": \"" + errorMessage + "\"}";
        resp.getWriter().write(errorResponse);
        e.printStackTrace();
    }
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
}
