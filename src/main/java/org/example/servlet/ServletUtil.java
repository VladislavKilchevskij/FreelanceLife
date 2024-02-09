package org.example.servlet;

import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;

import static java.util.stream.Collectors.joining;

public class ServletUtil {
    private ServletUtil() {
    }

    public static String getJsonBody(HttpServletRequest req) throws IOException {
        String jsonBody;
        try (var reader = req.getReader()) {
            jsonBody = reader.lines().collect(joining());
            return jsonBody;
        }
    }
}
