package org.example.servlet.handler;

import jakarta.servlet.http.HttpServletResponse;

public interface ExceptionHandler {
    void handleException(Exception exception, HttpServletResponse httpServletResponse);
}
