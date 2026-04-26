package com.cgp.taskboard.api;

import com.cgp.taskboard.api.http.DocsHandler;
import com.cgp.taskboard.api.task.TaskHandler;
import com.cgp.taskboard.api.task.TaskService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class TaskboardApiApplication {
    public static void main(String[] args) {
        int port = resolvePort();
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            TaskService taskService = new TaskService();

            server.createContext("/api/v1/tasks", new TaskHandler(taskService));
            server.createContext("/openapi.yaml", new DocsHandler.OpenApiHandler());
            server.createContext("/docs", new DocsHandler.HtmlDocsHandler());
            server.createContext("/", new DocsHandler.RootHandler());

            server.setExecutor(Executors.newCachedThreadPool());
            server.start();

            System.out.println("Taskboard API started on http://localhost:" + port);
            System.out.println("Docs: http://localhost:" + port + "/docs");
            System.out.println("OpenAPI: http://localhost:" + port + "/openapi.yaml");
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e);
        }
    }

    private static int resolvePort() {
        String value = System.getenv("PORT");
        if (value == null || value.isBlank()) {
            return 8080;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return 8080;
        }
    }
}
