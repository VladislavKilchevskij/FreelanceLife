package org.example.servlet.handler.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.example.servlet.handler.ExceptionHandler;

public class ExceptionHandlerImpl implements ExceptionHandler {

    @Override
    public void handleException(Exception exception, HttpServletResponse resp) {
        if (exception instanceof NumberFormatException) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
