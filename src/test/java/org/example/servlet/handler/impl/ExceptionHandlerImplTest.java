package org.example.servlet.handler.impl;

import jakarta.servlet.http.HttpServletResponse;
import org.example.servlet.handler.ExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerImplTest {

    private ExceptionHandler exceptionHandler;

    @Mock
    private HttpServletResponse mockResponse;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ExceptionHandlerImpl();
    }

    @Test
    void testHandleNumberFormatException() {
        exceptionHandler.handleException(new NumberFormatException(), mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Test
    void testHandleOtherException() {
        exceptionHandler.handleException(new Exception(), mockResponse);

        verify(mockResponse).setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}