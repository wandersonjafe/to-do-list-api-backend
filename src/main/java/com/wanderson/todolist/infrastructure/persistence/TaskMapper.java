package com.wanderson.todolist.infrastructure.persistence;

import com.wanderson.todolist.domain.model.Task;

public final class TaskMapper {

    private TaskMapper() {
    }

    public static TaskEntity toEntity(Task task) {
        return new TaskEntity(
                task.getId(),
                task.getOwnerId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt()
        );
    }

    public static Task toDomain(TaskEntity entity) {
        return Task.restore(
                entity.getId(),
                entity.getOwnerId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getStatus(),
                entity.getCreatedAt()
        );
    }
}