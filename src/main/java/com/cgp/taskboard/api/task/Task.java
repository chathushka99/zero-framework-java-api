package com.cgp.taskboard.api.task;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a task in the taskboard.
 *
 * <p>Instances are immutable; updates produce a new record.
 */
public record Task(
        UUID id, // ls
        String title, // ls
        String description, // ls
        boolean completed, // ls
        Instant createdAt, // ls
        Instant updatedAt
) {
}
