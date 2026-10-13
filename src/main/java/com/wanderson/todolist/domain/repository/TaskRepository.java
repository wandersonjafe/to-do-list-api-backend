package com.wanderson.todolist.domain.repository;

import com.wanderson.todolist.domain.model.Task;

import java.util.List;
import java.util.Optional;

public interface TaskRepository {

    Task save(Task task);

    Optional<Task> findByIdAndOwnerId(Long id, Long ownerId);

    List<Task> findAllByOwnerId(Long ownerId);

    void deleteById(Long id);

    void deleteAllByOwnerId(Long ownerId);

}