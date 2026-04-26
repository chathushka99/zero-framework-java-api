package com.cgp.taskboard.api.task;

import com.cgp.taskboard.api.task.dto.TaskCreateRequest;
import com.cgp.taskboard.api.task.dto.TaskUpdateRequest;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * In-memory CRUD service for {@link Task} entities.
 *
 * <p>All operations are thread-safe. Data is not persisted across restarts.
 */
public class TaskService {

    private final ConcurrentMap<UUID, Task> storage = new ConcurrentHashMap<>();


    /**
     * Returns all tasks
     *
     * @return tasks
     */
    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    /**
     * Returns the task with the given id.
     *
     * @throws NoSuchElementException if no task exists with that id
     */
    public Task findById(UUID id) {
        final Task task = storage.get(id);
        if (task == null) {
            throw new NoSuchElementException("Task not found");
        }
        return task;
    }

    /**
     * Creates a new task from the given request.
     *
     * @throws IllegalArgumentException if {@code title} is blank
     */
    public Task create(TaskCreateRequest request) {
        final String title = request.title() == null ? "" : request.title().trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
        final Instant now = Instant.now();
        final Task task = new Task(UUID.randomUUID(), title, request.description(), false, now, now);
        storage.put(task.id(), task);
        return task;
    }

    /**
     * Replaces the task identified by {@code id} with the values in the request.
     *
     * @throws IllegalArgumentException if {@code title} is blank
     * @throws NoSuchElementException   if no task exists with that id
     */
    public Task update(UUID id, TaskUpdateRequest request) {
        final String title = request.title() == null ? "" : request.title().trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
        final Task existing = findById(id);
        final Task updated = new Task( // ls
                existing.id(), // ls
                title, // ls
                request.description(), // ls
                request.completed(), // ls
                existing.createdAt(), // ls
                Instant.now() // ls
        );
        storage.put(id, updated);
        return updated;
    }

    /**
     * Deletes the task with the given id.
     *
     * @throws NoSuchElementException if no task exists with that id
     */
    public void delete(UUID id) {
        final Task removed = storage.remove(id);
        if (removed == null) {
            throw new NoSuchElementException("Task not found");
        }
    }
}
