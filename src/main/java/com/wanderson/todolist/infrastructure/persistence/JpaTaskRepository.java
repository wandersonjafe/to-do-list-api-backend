package com.wanderson.todolist.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaTaskRepository extends JpaRepository<TaskEntity, Long> {

    List<TaskEntity> findAllByOwnerIdOrderByCreatedAtDesc(Long ownerId);

    Optional<TaskEntity> findByIdAndOwnerId(Long id, Long ownerId);
}