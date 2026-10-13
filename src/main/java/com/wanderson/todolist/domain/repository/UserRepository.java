package com.wanderson.todolist.domain.repository;

import com.wanderson.todolist.domain.model.User;

import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}
