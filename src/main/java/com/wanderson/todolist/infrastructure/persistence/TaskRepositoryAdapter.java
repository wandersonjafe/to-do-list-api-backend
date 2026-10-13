package com.wanderson.todolist.infrastructure.persistence;

import com.wanderson.todolist.domain.model.Task;
import com.wanderson.todolist.domain.repository.TaskRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class TaskRepositoryAdapter implements TaskRepository {

    private final JpaTaskRepository jpaRepository;

    public TaskRepositoryAdapter(JpaTaskRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Task save(Task task) {
        TaskEntity saved = jpaRepository.save(TaskMapper.toEntity(task));
        return TaskMapper.toDomain(saved);
    }

    @Override
    public Optional<Task> findByIdAndOwnerId(Long id, Long ownerId) {
        return jpaRepository.findByIdAndOwnerId(id, ownerId).map(TaskMapper::toDomain);
    }

    @Override
    public List<Task> findAllByOwnerId(Long ownerId) {
        return jpaRepository.findAllByOwnerIdOrderByCreatedAtDesc(ownerId).stream()
                .map(TaskMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public void deleteAllByOwnerId(Long ownerId) {
        jpaRepository.deleteByOwnerId(ownerId);
    }
}