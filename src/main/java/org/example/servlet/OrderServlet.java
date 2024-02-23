package org.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.service.OrderService;
import org.example.service.impl.OrderServiceImpl;
import org.example.servlet.handler.ExceptionHandler;
import org.example.servlet.handler.impl.ExceptionHandlerImpl;
import org.example.servlet.mapper.OrderJsonMapper;
import org.example.servlet.mapper.impl.OrderJsonMapperImpl;

import java.io.IOException;

@WebServlet(name = "OrderServlet", value = "/orders")
public class OrderServlet extends HttpServlet {
    private final OrderService service;
    private final OrderJsonMapper jsonMapper;
    private final ExceptionHandler exceptionHandler;


    private static final String CONTENT_JSON = "application/json";
    private static final String PARAMETER_ID = "id";

    public OrderServlet() {
        this.service = new OrderServiceImpl();
        this.jsonMapper = new OrderJsonMapperImpl();
        this.exceptionHandler = new ExceptionHandlerImpl();
    }

    public OrderServlet(OrderService service, OrderJsonMapper jsonMapper, ExceptionHandler exceptionHandler) {
        this.service = service;
        this.jsonMapper = jsonMapper;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String param = req.getParameter(PARAMETER_ID);
            String jsonResponse;
            if (param != null) {
                Long id = Long.parseLong(param);
                var resultDto = service.findById(id);
                if (resultDto == null) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                jsonResponse = jsonMapper.toJson(resultDto);
            } else {
                var dtos = service.findAll();
                if (dtos.isEmpty()) {
                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                    return;
                }
                jsonResponse = jsonMapper.toJson(dtos);
            }
            resp.setContentType(CONTENT_JSON);
            resp.setStatus(HttpServletResponse.SC_OK);
            ServletUtil.sendJsonResponse(jsonResponse, resp);
        } catch (NumberFormatException | IOException e) {
            exceptionHandler.handleException(e, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        try {
            String jsonBody = ServletUtil.getJsonBody(req);
            var bodyDto = jsonMapper.toDto(jsonBody);
            var resultDto = service.save(bodyDto);
            if (resultDto != null && resultDto.getId() != null) {
                String jsonResp = jsonMapper.toJson(resultDto);
                resp.setContentType(CONTENT_JSON);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                ServletUtil.sendJsonResponse(jsonResp, resp);
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
            var updatedDto = jsonMapper.toDto(jsonBody);
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
