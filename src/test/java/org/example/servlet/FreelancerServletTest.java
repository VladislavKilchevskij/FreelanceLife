package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    void beforeEach() {
        servlet = new FreelancerServlet(service, jsonMapper, exceptionHandler);
    }

    @Test
    void doGetWhenParameterPresentAndValidThenReturnDto() throws IOException {
        var dto = new FreelancerDto();
        var mockWriter = mock(PrintWriter.class);

        when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
        when(service.findById(LONG_ID)).thenReturn(dto);
        when(jsonMapper.toJson(dto)).thenReturn(JSON_TEST);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).findById(LONG_ID);
        verify(jsonMapper, times(1)).toJson(dto);
        verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).getWriter();
        verify(mockWriter, times(1)).println(anyString());
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
    void doGetWhenParameterAbsentThenReturnList() throws IOException {
        List<FreelancerSimpleDto> freelancers = Arrays.asList(new FreelancerSimpleDto(), new FreelancerSimpleDto());
        var mockWriter = mock(PrintWriter.class);

        when(request.getParameter(PARAMETER_ID)).thenReturn(null);
        when(service.findAll()).thenReturn(freelancers);
        when(jsonMapper.toJson(freelancers)).thenReturn(JSON_TEST);
        when(response.getWriter()).thenReturn(mockWriter);

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).findAll();
        verify(jsonMapper, times(1)).toJson(freelancers);
        verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).getWriter();
        verify(mockWriter, times(1)).println(anyString());
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
    void doGetWhenDtoWriterThrowsIOExceptionThenHandleException() throws IOException {
        var dto = new FreelancerDto();

        when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
        when(service.findById(LONG_ID)).thenReturn(dto);
        when(jsonMapper.toJson(dto)).thenReturn(JSON_TEST);
        when(response.getWriter()).thenThrow(new IOException("Mocked IOException"));

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).findById(LONG_ID);
        verify(jsonMapper, times(1)).toJson(dto);
        verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).getWriter();
        verify(exceptionHandler, times(1)).handleException(any(IOException.class), eq(response));
    }

    @Test
    void doGetWhenListWriterThrowsIOExceptionThenHandleException() throws IOException {
        var dto = new FreelancerDto();

        when(request.getParameter(PARAMETER_ID)).thenReturn(String.valueOf(LONG_ID));
        when(service.findById(LONG_ID)).thenReturn(dto);
        when(jsonMapper.toJson(dto)).thenReturn(JSON_TEST);
        when(response.getWriter()).thenThrow(new IOException("Mocked IOException"));

        servlet.doGet(request, response);

        verify(request, times(1)).getParameter(PARAMETER_ID);
        verify(service, times(1)).findById(LONG_ID);
        verify(jsonMapper, times(1)).toJson(dto);
        verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
        verify(response, times(1)).setStatus(HttpServletResponse.SC_OK);
        verify(response, times(1)).getWriter();
        verify(exceptionHandler, times(1)).handleException(any(IOException.class), eq(response));
    }

    @Test
    void doPostWhenBodyPresentedAndSaveSuccesThenCreated() throws IOException {
        String jsonBody = "test";
        var dto = spy(FreelancerDto.class);
        var dtoSaved = spy(FreelancerDto.class);
        var printWriter = mock(PrintWriter.class);
        dtoSaved.setId(1L);

        try(MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(dtoSaved);
            when(jsonMapper.toJson(dtoSaved)).thenReturn(jsonBody);
            when(response.getWriter()).thenReturn(printWriter);

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(FreelancerDto.class));
            verify(jsonMapper, times(1)).toJson(any(FreelancerDto.class));
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
            verify(response, times(1)).getWriter();
            verify(printWriter, times(1)).println(anyString());
        }
    }

    @Test
    void doPostWhenBodyPresentedAndSaveFailThenBadRequest() throws IOException {
        String jsonBody = "test";
        var dto = spy(FreelancerDto.class);
        var dtoSaved = spy(FreelancerDto.class);
        dtoSaved.setId(1L);

        try(MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
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
    void doPostWhenWriterThrowsIOThenHandleException() throws IOException {
        String jsonBody = "test";
        var dto = spy(FreelancerDto.class);
        var dtoSaved = spy(FreelancerDto.class);
        dtoSaved.setId(1L);

        try(MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
            mockedStatic.when(() -> ServletUtil.getJsonBody(request)).thenReturn(jsonBody);
            when(jsonMapper.toDto(jsonBody)).thenReturn(dto);
            when(service.save(dto)).thenReturn(dtoSaved);
            when(jsonMapper.toJson(dtoSaved)).thenReturn(jsonBody);
            when(response.getWriter()).thenThrow(new IOException("Mocked IOException"));

            servlet.doPost(request, response);

            mockedStatic.verify(() -> ServletUtil.getJsonBody(request), times(1));
            verify(jsonMapper, times(1)).toDto(anyString());
            verify(service, times(1)).save(any(FreelancerDto.class));
            verify(jsonMapper, times(1)).toJson(any(FreelancerDto.class));
            verify(response, times(1)).setContentType(JSON_CONTENT_TYPE);
            verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
            verify(response, times(1)).getWriter();
            verify(exceptionHandler, times(1)).handleException(any(IOException.class), eq(response));
        }
    }

    @Test
    void doUpdateWhenBodyPresentedAndUpdateSuccesThenCreated() {
        String jsonBody = "test";
        var dtoUpdate = spy(FreelancerDto.class);
        dtoUpdate.setId(1L);

        try(MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
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
    void doUpdateWhenBodyPresentedAndUpdateFailThenBadRequest() {
        String jsonBody = "test";
        var dtoUpdate = spy(FreelancerDto.class);
        dtoUpdate.setId(1L);

        try(MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
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
    void doUpdateWhenWriterThrowsIOThenHandleException() {
        var dtoSaved = spy(FreelancerDto.class);
        dtoSaved.setId(1L);

        try(MockedStatic<ServletUtil> mockedStatic = mockStatic(ServletUtil.class)) {
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