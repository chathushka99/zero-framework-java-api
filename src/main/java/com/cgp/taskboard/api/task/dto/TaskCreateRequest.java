package com.cgp.taskboard.api.task.dto;

/** Request body for creating a new task. {@code title} is required; {@code description} is optional. */
public record TaskCreateRequest(String title, String description) {}
