package com.cgp.taskboard.api.task.dto;

/** Request body for updating an existing task. All fields must be supplied. */
public record TaskUpdateRequest(String title, String description, boolean completed) {}
