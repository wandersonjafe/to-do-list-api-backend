package com.wanderson.todolist.domain.model;

import java.time.Instant;

public class Task {

    private static final int TITLE_MAX_LENGTH = 120;

    private final Long id;
    private final Long ownerId;
    private String title;
    private String description;
    private TaskStatus status;
    private final Instant createdAt;

    private Task(Long id, Long ownerId, String title, String description,
                 TaskStatus status, Instant createdAt) {
        this.id = id;
        this.ownerId = ownerId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.createdAt = createdAt;
    }

    // Cria uma tarefa nova (ainda sem id, quem gera é o banco)
    public static Task create(Long ownerId, String title, String description) {
        return new Task(null, ownerId, validateTitle(title), description, TaskStatus.PENDING, Instant.now());
    }

    // Reconstrói uma tarefa já existente (usado pela camada de persistência)
    public static Task restore(Long id, Long ownerId, String title, String description,
                               TaskStatus status, Instant createdAt) {
        return new Task(id, ownerId, title, description, status, createdAt);
    }

    public void update(String title, String description) {
        this.title = validateTitle(title);
        this.description = description;
    }

    public void complete() {
        this.status = TaskStatus.COMPLETED;
    }

    public void reopen() {
        this.status = TaskStatus.PENDING;
    }

    private static String validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("O título é obrigatório");
        }
        if (title.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException("O título deve ter no máximo " + TITLE_MAX_LENGTH + " caracteres");
        }
        return title.trim();
    }

    public Long getId() { return id; }
    public Long getOwnerId() { return ownerId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public TaskStatus getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
}