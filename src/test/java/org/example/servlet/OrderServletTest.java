package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.OrderService;
import org.example.service.dto.OrderDto;
import org.example.service.dto.OrderSimpleDto;
import org.example.servlet.handler.ExceptionHandler;
import org.example.servlet.mapper.OrderJsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServletTest {
    @Mock
    private OrderService service;
    @Mock
    private OrderJsonMapper jsonMapper;
    @Mock
    private ExceptionHandler exceptionHandler;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private OrderServlet servlet;

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String PARAMETER_ID = "id";
    private static final String JSON_TEST = "test";
    private static final Long LONG_ID = 1L;

    @BeforeEach
    void setup() {
        servlet = new OrderServlet(service, jsonMapper, exceptionHandler);
    }

    @Test
    void testConstructor() {
        var orderServlet = new OrderServlet();
        assertNotNull(orderServlet);
    }

    @Test
    void doGetWhenParameterPresentAndValidThenReturnDto() {
        var dtoMock = mock(OrderDto.class);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
            when(service.findById(LONG_ID)).thenReturn(dtoMock);
            when(jsonMapper.toJson(dtoMock)).thenReturn(JSON_TEST);

            servlet.doGet(request, response);

            verify(request, times(1)).getParameter(PARAMETER_ID);
            verify(service, times(1)).findById(LONG_ID);
            verify(jsonMapper, times(1)).toJson(dtoMock);
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
            mockedStatic.verify(() -> ServletUtil.sendJsonResponse(anyString(), eq(response)), times(1));
        }
    }

    @Test
    void doGetWhenParameterInvalidThenHandleNumberFormatException() {
        when(request.getParameter(PARAMETER_ID)).thenReturn("invalidId");

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(exceptionHandler, times(1)).handleException(any(NumberFormatException.class), eq(response));
    }

    @Test
    void doGetWhenNullDtoThenResponseNotFound() {
        when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
        when(service.findById(LONG_ID)).thenReturn(null);

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).findById(LONG_ID);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetWhenParameterAbsentThenReturnList() {
        List<OrderSimpleDto> orders = Arrays.asList(new OrderSimpleDto(), new OrderSimpleDto());

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            when(request.getParameter(PARAMETER_ID)).thenReturn(null);
            when(service.findAll()).thenReturn(orders);
            when(jsonMapper.toJson(orders)).thenReturn(JSON_TEST);

            servlet.doGet(request, response);

            verify(request, times(1)).getParameter(PARAMETER_ID);
            verify(service, times(1)).findAll();
            verify(jsonMapper, times(1)).toJson(orders);
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
            mockedStatic.verify(() -> ServletUtil.sendJsonResponse(anyString(), eq(response)));
        }

    }

    @Test
    void doGetWhenEmptyListThenResponseNotFound() {
        when(request.getParameter(PARAMETER_ID)).thenReturn(null);
        when(service.findAll()).thenReturn(new ArrayList<>());

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).findAll();
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Test
    void doGetWhenSendJsonThrowsIOExceptionThenHandleException() {
        var dto = mock(OrderDto.class);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
            when(service.findById(LONG_ID)).thenReturn(dto);
            when(jsonMapper.toJson(dto)).thenReturn(JSON_TEST);
            mockedStatic.when(() -> ServletUtil.sendJsonResponse(JSON_TEST, response)).thenThrow(new IOException("Mocked IOException"));

            servlet.doGet(request, response);

            verify(request, times(1)).getParameter(PARAMETER_ID);
            verify(service, times(1)).findById(LONG_ID);
            verify(jsonMapper, times(1)).toJson(dto);
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
            mockedStatic.verify(() -> ServletUtil.sendJsonResponse(anyString(), eq(response)));
            verify(exceptionHandler, times(1)).handleException(any(IOException.class), eq(response));
        }
    }

    @Test
    void doPostWhenBodyPresentedAndSaveSuccessThenCreated() {
        String jsonBody = "test";
        var dto = spy(OrderDto.class);
        var dtoSaved = spy(OrderDto.class);
        dtoSaved.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(dtoSaved);
            when(jsonMapper.toJson(dtoSaved)).thenReturn(jsonBody);

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(OrderDto.class));
            verify(jsonMapper, times(1)).toJson(any(OrderDto.class));
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
            mockedStatic.verify(() -> ServletUtil.sendJsonResponse(jsonBody, response), times(1));
        }
    }

    @Test
    void doPostWhenBodyPresentedAndSaveFailThenBadRequest() {
        String jsonBody = "test";
        var dto = spy(OrderDto.class);
        var dtoSaved = spy(OrderDto.class);
        dtoSaved.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(null);

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(OrderDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPostWhenBodyPresentedAndDtoIdNullThenBadRequest() {
        String jsonBody = "test";
        var dto = spy(OrderDto.class);
        var dtoSaved = spy(OrderDto.class);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(dtoSaved);

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(OrderDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPostWhenSendJsonThrowsIOThenHandleException() {
        String jsonBody = "test";
        var dto = spy(OrderDto.class);
        var dtoSaved = spy(OrderDto.class);
        dtoSaved.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(dtoSaved);
            when(jsonMapper.toJson(dtoSaved)).thenReturn(jsonBody);
            mockedStatic.when(() -> ServletUtil.sendJsonResponse(jsonBody, response)).thenThrow(new IOException("Mocked"));

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(OrderDto.class));
            verify(jsonMapper, times(1)).toJson(any(OrderDto.class));
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
            mockedStatic.verify(() -> ServletUtil.sendJsonResponse(anyString(), eq(response)));
            verify(exceptionHandler, times(1)).handleException(any(IOException.class), eq(response));
        }
    }

    @Test
    void doPostWhenGetJsonBodyThrowsIOThenHandleException() {
        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenThrow(new IOException("Mock"));

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(exceptionHandler, times(1)).handleException(any(IOException.class), eq(response));
        }
    }

    @Test
    void doPutWhenBodyPresentedAndUpdateSuccessThenCreated() {
        String jsonBody = "test";
        var dtoUpdate = spy(OrderDto.class);
        dtoUpdate.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dtoUpdate);
            when(service.update(dtoUpdate)).thenReturn(true);

            servlet.doPut(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).update(any(OrderDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void doPutWhenBodyPresentedAndUpdateFailThenBadRequest() {
        String jsonBody = "test";
        var dtoUpdate = spy(OrderDto.class);
        dtoUpdate.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dtoUpdate);
            when(service.update(dtoUpdate)).thenReturn(false);

            servlet.doPut(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).update(any(OrderDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPutWhenBodyPresentedMapperReturnNullThenBadRequest() {
        String jsonBody = "test";

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(null);
            when(service.update(null)).thenReturn(false);

            servlet.doPut(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(jsonBody);
            verify(service, times(1)).update(null);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPutWhenGetJsonBodyThrowsIOThenHandleException() {
        var dtoSaved = spy(OrderDto.class);
        dtoSaved.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenThrow(new IOException("Mocked IOException"));

            servlet.doPut(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(exceptionHandler, times(1)).handleException(any(IOException.class), eq(response));
        }
    }

    @Test
    void doDeleteWhenParameterInvalidThenHandleNumberFormatException() {
        when(request.getParameter(PARAMETER_ID)).thenReturn("invalidId");

        servlet.doDelete(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(exceptionHandler, times(1)).handleException(any(NumberFormatException.class), eq(response));
    }

    @Test
    void doDeleteWhenParameterValidThenDelete() {
        when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
        when(service.delete(LONG_ID)).thenReturn(true);
        servlet.doDelete(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).delete(LONG_ID);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    @Test
    void doDeleteWhenNoSuchDtoThenResponseBadRequest() {
        when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
        when(service.delete(LONG_ID)).thenReturn(false);

        servlet.doDelete(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).delete(LONG_ID);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
    }
}