package com.wanderson.todolist.application.usecase;

import com.wanderson.todolist.application.dto.TaskResponse;
import com.wanderson.todolist.domain.exception.TaskNotFoundException;
import com.wanderson.todolist.domain.model.Task;
import com.wanderson.todolist.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompleteTaskUseCase {

    private final TaskRepository repository;

    public CompleteTaskUseCase(TaskRepository repository) {
        this.repository = repository;

    }

    @Transactional
    public TaskResponse execute (Long userId, Long id) {
        Task task = repository.findByIdAndOwnerId(id, userId)
                .orElseThrow(() -> new TaskNotFoundException(id));
        task.complete();
        return TaskResponse.from(repository.save(task));
    }
}
