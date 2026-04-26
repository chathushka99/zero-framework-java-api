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

public class TaskService {

    private final ConcurrentMap<UUID, Task> storage = new ConcurrentHashMap<>();

    public List<Task> findAll() {
        return new ArrayList<>(storage.values());
    }

    public Task findById(UUID id) {
        Task task = storage.get(id);
        if (task == null) {
            throw new NoSuchElementException("Task not found");
        }
        return task;
    }

    public Task create(TaskCreateRequest request) {
        String title = request.getTitle() == null ? "" : request.getTitle().trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
        Instant now = Instant.now();
        Task task = new Task(
                UUID.randomUUID(),
                title,
                request.getDescription(),
                false,
                now,
                now
        );
        storage.put(task.getId(), task);
        return task;
    }

    public Task update(UUID id, TaskUpdateRequest request) {
        String title = request.getTitle() == null ? "" : request.getTitle().trim();
        if (title.isEmpty()) {
            throw new IllegalArgumentException("title is required");
        }
        Task existing = findById(id);
        existing.setTitle(title);
        existing.setDescription(request.getDescription());
        existing.setCompleted(request.isCompleted());
        existing.setUpdatedAt(Instant.now());
        storage.put(id, existing);
        return existing;
    }

    public void delete(UUID id) {
        Task removed = storage.remove(id);
        if (removed == null) {
            throw new NoSuchElementException("Task not found");
        }
    }
}
