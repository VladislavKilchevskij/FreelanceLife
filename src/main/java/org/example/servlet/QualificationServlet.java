package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.QualificationService;
import org.example.service.impl.QualificationServiceImpl;
import org.example.servlet.handler.ExceptionHandler;
import org.example.servlet.handler.impl.ExceptionHandlerImpl;
import org.example.servlet.mapper.QualificationJsonMapper;
import org.example.servlet.mapper.impl.QualificationJsonMapperImpl;

import java.io.IOException;

@WebServlet(name = "QualificationServlet", value = "/qualifications")
public class QualificationServlet extends HttpServlet {
    private final QualificationService service;
    private final QualificationJsonMapper jsonMapper;

    private final ExceptionHandler exceptionHandler;


    public QualificationServlet() {
        this.service = new QualificationServiceImpl();
        this.jsonMapper = new QualificationJsonMapperImpl();
        this.exceptionHandler = new ExceptionHandlerImpl();
    }

    public QualificationServlet(QualificationService service, QualificationJsonMapper jsonMapper, ExceptionHandler exceptionHandler) {
        this.service = service;
        this.jsonMapper = jsonMapper;
        this.exceptionHandler = exceptionHandler;
    }

    private static final String CONTENT_JSON = "application/json";

    private static final String PARAMETER_ID = "id";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String param = req.getParameter(PARAMETER_ID);
            String jasonResponse;
            if (param != null) {
                Long id = Long.parseLong(param);
                var resultDto = service.findById(id);
                if (resultDto == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                jasonResponse = jsonMapper.toJson(resultDto);
            } else {
                var dtos = service.findAll();
                if (dtos.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                jasonResponse = jsonMapper.toJson(dtos);
            }
            resp.setContentType(CONTENT_JSON);
            resp.setStatus(HttpServletResponse.SC_OK);
            try (var out = resp.getWriter()) {
                out.println(jasonResponse);
            }
        } catch (NumberFormatException | IOException e) {
            exceptionHandler.handleException(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String jsonBody = ServletUtil.getJsonBody(req);
            var bodyDto = jsonMapper.toSimpleDto(jsonBody);
            var resultDto = service.save(bodyDto);
            if (resultDto != null && resultDto.getId() != null) {
                String jsonResp = jsonMapper.toJson(resultDto);
                resp.setContentType(CONTENT_JSON);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                try (var out = resp.getWriter()) {
                    out.println(jsonResp);
                }
            }
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            exceptionHandler.handleException(e, resp);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String jsonBody = ServletUtil.getJsonBody(req);
            var updatedDto = jsonMapper.toSimpleDto(jsonBody);
            if (service.update(updatedDto)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (IOException e) {
            exceptionHandler.handleException(e, resp);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        try {
            Long id = Long.parseLong(req.getParameter(PARAMETER_ID));
            if (service.delete(id)) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NumberFormatException e) {
            exceptionHandler.handleException(e, resp);
        }
    }
}