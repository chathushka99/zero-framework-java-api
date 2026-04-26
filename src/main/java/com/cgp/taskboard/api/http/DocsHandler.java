package com.cgp.taskboard.api.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public final class DocsHandler {
    private DocsHandler() {
    }

    public static class RootHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Location", "/docs");
            exchange.sendResponseHeaders(302, -1);
            exchange.close();
        }
    }

    public static class OpenApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }
            byte[] body = readResource("/openapi.yaml");
            exchange.getResponseHeaders().set("Content-Type", "application/yaml; charset=utf-8");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        }
    }

    public static class HtmlDocsHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(405, -1);
                exchange.close();
                return;
            }
            byte[] body = readResource("/docs.html");
            exchange.getResponseHeaders().set("Content-Type", "text/html; charset=utf-8");
            exchange.sendResponseHeaders(200, body.length);
            exchange.getResponseBody().write(body);
            exchange.close();
        }
    }

    private static byte[] readResource(String path) throws IOException {
        try (InputStream inputStream = DocsHandler.class.getResourceAsStream(path)) {
            if (inputStream == null) {
                return "Resource not found".getBytes(StandardCharsets.UTF_8);
            }
            return inputStream.readAllBytes();
        }
    }
}
