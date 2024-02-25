package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.db.impl.ConnectionManagerImpl;
import org.example.service.FreelancerService;
import org.example.service.dto.*;
import org.example.servlet.handler.ExceptionHandler;
import org.example.servlet.mapper.FreelancerJsonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FreelancerServletTest {
    @Mock
    private FreelancerService service;
    @Mock
    private FreelancerJsonMapper jsonMapper;
    @Mock
    private ExceptionHandler exceptionHandler;
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    private FreelancerServlet servlet;

    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String PARAMETER_ID = "id";
    private static final String JSON_TEST = "test";
    private static final Long LONG_ID = 1L;

    @BeforeEach
    void setup() {
        servlet = new FreelancerServlet(service, jsonMapper, exceptionHandler);
    }

    @Test
    void testConstructorWithoutParameters() {
        FreelancerServlet servletWithoutParameters;
        try(MockedStatic<ConnectionManagerImpl> mockedStatic = mockStatic(ConnectionManagerImpl.class)) {
            mockedStatic.when(ConnectionManagerImpl::getInstance).thenReturn(mock(ConnectionManagerImpl.class));
            servletWithoutParameters = new FreelancerServlet();
        }

        assertNotNull(servletWithoutParameters);
    }

    @Test
    void doGetWhenParameterPresentAndValidThenReturnDto() {
        var dtoMock = mock(FreelancerDto.class);

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
        List<FreelancerSimpleDto> orders = Arrays.asList(new FreelancerSimpleDto(), new FreelancerSimpleDto());

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
        var dto = mock(FreelancerDto.class);

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
        var dto = spy(FreelancerDto.class);
        var dtoSaved = spy(FreelancerDto.class);
        dtoSaved.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(dtoSaved);
            when(jsonMapper.toJson(dtoSaved)).thenReturn(jsonBody);

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(FreelancerDto.class));
            verify(jsonMapper, times(1)).toJson(any(FreelancerDto.class));
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
            mockedStatic.verify(() -> ServletUtil.sendJsonResponse(jsonBody, response), times(1));
        }
    }

    @Test
    void doPostWhenBodyPresentedAndSaveFailThenBadRequest() {
        String jsonBody = "test";
        var dto = spy(FreelancerDto.class);
        var dtoSaved = spy(FreelancerDto.class);
        dtoSaved.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(null);

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(FreelancerDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPostWhenBodyPresentedAndDtoIdNullThenBadRequest() {
        String jsonBody = "test";
        var dto = spy(FreelancerDto.class);
        var dtoSaved = spy(FreelancerDto.class);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(dtoSaved);

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(FreelancerDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPostWhenSendJsonThrowsIOThenHandleException() {
        String jsonBody = "test";
        var dto = spy(FreelancerDto.class);
        var dtoSaved = spy(FreelancerDto.class);
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
            verify(service, times(1)).save(any(FreelancerDto.class));
            verify(jsonMapper, times(1)).toJson(any(FreelancerDto.class));
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
        var dtoUpdate = spy(FreelancerDto.class);
        dtoUpdate.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dtoUpdate);
            when(service.update(dtoUpdate)).thenReturn(true);

            servlet.doPut(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).update(any(FreelancerDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        }
    }

    @Test
    void doPutWhenBodyPresentedAndUpdateFailThenBadRequest() {
        String jsonBody = "test";
        var dtoUpdate = spy(FreelancerDto.class);
        dtoUpdate.setId(1L);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dtoUpdate);
            when(service.update(dtoUpdate)).thenReturn(false);

            servlet.doPut(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).update(any(FreelancerDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPutWhenBodyPresentedButWithNullIdThenBadRequest() {
        String jsonBody = "test";
        var dto = spy(FreelancerDto.class);

        try (MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);

            servlet.doPut(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(jsonBody);
            verify(service, never()).update(any(FreelancerDto.class));
            verify(response, times(1)).setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Test
    void doPutWhenGetJsonBodyThrowsIOThenHandleException() {
        var dtoSaved = spy(FreelancerDto.class);
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