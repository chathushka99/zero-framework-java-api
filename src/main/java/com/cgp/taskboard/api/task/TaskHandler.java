package com.cgp.taskboard.api.task;

import com.cgp.taskboard.api.task.dto.TaskCreateRequest;
import com.cgp.taskboard.api.task.dto.TaskUpdateRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * HTTP handler for the {@code /api/v1/tasks} resource.
 *
 * <p>Routes:
 * <ul>
 *   <li>{@code GET    /api/v1/tasks}      – list all tasks
 *   <li>{@code POST   /api/v1/tasks}      – create a task
 *   <li>{@code GET    /api/v1/tasks/{id}} – get a task by id
 *   <li>{@code PUT    /api/v1/tasks/{id}} – replace a task
 *   <li>{@code DELETE /api/v1/tasks/{id}} – delete a task
 * </ul>
 */
public class TaskHandler implements HttpHandler {

    private static final String BASE_PATH = "/api/v1/tasks";

    private final TaskService taskService;
    private final ObjectMapper objectMapper;

    public TaskHandler(TaskService taskService) {
        this.taskService = taskService;
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String path = exchange.getRequestURI().getPath();

            if (BASE_PATH.equals(path) || (BASE_PATH + "/").equals(path)) {
                handleCollection(exchange);
                return;
            }

            if (path.startsWith(BASE_PATH + "/")) {
                handleItem(exchange, path.substring((BASE_PATH + "/").length()));
                return;
            }

            sendJson(exchange, 404, Map.of("error", "Not found"));
        } catch (IllegalArgumentException e) {
            sendJson(exchange, 400, Map.of("error", e.getMessage()));
        } catch (NoSuchElementException e) {
            sendJson(exchange, 404, Map.of("error", e.getMessage()));
        } catch (JsonProcessingException e) {
            sendJson(exchange, 400, Map.of("error", "Invalid JSON payload"));
        } catch (Exception e) {
            sendJson(exchange, 500, Map.of("error", "Internal server error"));
        } finally {
            exchange.close();
        }
    }

    private void handleCollection(HttpExchange exchange) throws IOException {
        switch (exchange.getRequestMethod()) {
            case "GET" -> sendJson(exchange, 200, taskService.findAll());
            case "POST" -> {
                TaskCreateRequest request = readRequestBody(exchange, TaskCreateRequest.class);
                Task created = taskService.create(request);
                exchange.getResponseHeaders().set("Location", BASE_PATH + "/" + created.id());
                sendJson(exchange, 201, created);
            }
            default -> sendJson(exchange, 405, Map.of("error", "Method not allowed"));
        }
    }

    private void handleItem(HttpExchange exchange, String idPart) throws IOException {
        if (idPart.isBlank() || idPart.contains("/")) {
            sendJson(exchange, 404, Map.of("error", "Not found"));
            return;
        }

        UUID id;
        try {
            id = UUID.fromString(idPart);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid task id");
        }

        switch (exchange.getRequestMethod()) {
            case "GET" -> sendJson(exchange, 200, taskService.findById(id));
            case "PUT" -> {
                TaskUpdateRequest request = readRequestBody(exchange, TaskUpdateRequest.class);
                sendJson(exchange, 200, taskService.update(id, request));
            }
            case "DELETE" -> {
                taskService.delete(id);
                exchange.sendResponseHeaders(204, -1);
            }
            default -> sendJson(exchange, 405, Map.of("error", "Method not allowed"));
        }
    }

    private <T> T readRequestBody(HttpExchange exchange, Class<T> type) throws IOException {
        byte[] body = exchange.getRequestBody().readAllBytes();
        return objectMapper.readValue(body, type);
    }

    private void sendJson(HttpExchange exchange, int status, Object payload) throws IOException {
        byte[] responseBody = objectMapper.writeValueAsBytes(payload);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, responseBody.length);
        exchange.getResponseBody().write(responseBody);
    }
}
