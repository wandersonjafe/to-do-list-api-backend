package com.wanderson.todolist.application.usecase;

import com.wanderson.todolist.domain.exception.TaskNotFoundException;
import com.wanderson.todolist.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteTaskUseCase {

    private final TaskRepository repository;

    public DeleteTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public void execute (Long userId, Long id) {
        repository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        repository.deleteById(id);
    }
}
