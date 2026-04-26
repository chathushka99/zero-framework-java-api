package com.cgp.taskboard.api;

import com.cgp.taskboard.api.task.TaskHandler;
import com.cgp.taskboard.api.task.TaskService;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

/**
 * Application entry point.
 *
 * <p>Starts a {@link HttpServer} on the port defined by the {@code PORT} environment variable,
 * defaulting to {@code 8080}.
 */
public class TaskboardApiApplication {

    public static void main(String[] args) {
        int port = resolvePort();
        try {
            final HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            final TaskService taskService = new TaskService();

            server.createContext("/api/v1/tasks", new TaskHandler(taskService));
            server.setExecutor(Executors.newVirtualThreadPerTaskExecutor());
            server.start();

            System.out.println("Taskboard API started on http://localhost:" + port); // TODO: replace by log
        } catch (IOException e) {
            throw new RuntimeException("Failed to start server", e); // TODO: add custom exception
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
