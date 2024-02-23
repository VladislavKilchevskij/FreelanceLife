package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

import static java.util.stream.Collectors.joining;

public class ServletUtil {
    private ServletUtil() {
    }

    static String getJsonBody(HttpServletRequest req) throws IOException {
        String jsonBody;
        try (var reader = req.getReader()) {
            jsonBody = reader.lines().collect(joining());
            return jsonBody;
        }
    }

    static void sendJsonResponse(String json, HttpServletResponse resp) throws IOException {
        try(var out = resp.getWriter()) {
            out.println(json);
        }
    }
}