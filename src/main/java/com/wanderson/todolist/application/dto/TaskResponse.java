package com.wanderson.todolist.application.dto;

import com.wanderson.todolist.domain.model.Task;
import com.wanderson.todolist.domain.model.TaskStatus;

import java.time.Instant;

public record TaskResponse (
        Long id,
        String title,
        String description,
        TaskStatus status,
        Instant createdAt
) {
    public static TaskResponse from (Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }
}
