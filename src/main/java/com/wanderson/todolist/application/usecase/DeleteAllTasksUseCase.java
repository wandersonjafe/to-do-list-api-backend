package com.wanderson.todolist.application.usecase;

import com.wanderson.todolist.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteAllTasksUseCase {

    private final TaskRepository repository;

    public DeleteAllTasksUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute(Long userId) {
        repository.deleteAllByOwnerId(userId);
    }
}