package com.wanderson.todolist.domain.model;

import java.time.Instant;

public class User {

    private final Long id;
    private final String email;
    private final String passwordHash;
    private final Instant createdAt;

    private User(Long id, String email, String passwordHash, Instant createdAt ) {
        this.id = id;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;

    }

    public static User create(String email, String passwordHash) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório");
        }

        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória");
        }
        return new User(null, email, passwordHash, Instant.now());
    }

    public static User restore(Long id, String email, String passwordHash, Instant createdAt) {
        return new User(id, email, passwordHash, createdAt);
    }

    public Long getId() { return id; }
    public String getEmail() { return email; }
    public String getPasswordHash() { return passwordHash; }
    public Instant getCreatedAt() { return createdAt; }
}
