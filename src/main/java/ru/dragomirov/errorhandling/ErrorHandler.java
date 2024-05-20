package ru.dragomirov.errorhandling;

import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ErrorHandler {
    void httpErrors(HttpServletResponse resp, String errorMessage) throws IOException;
}
