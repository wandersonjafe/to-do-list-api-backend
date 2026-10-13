package com.wanderson.todolist.application.usecase;

import com.wanderson.todolist.application.dto.CreateTaskCommand;
import com.wanderson.todolist.application.dto.TaskResponse;
import com.wanderson.todolist.domain.model.Task;
import com.wanderson.todolist.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTaskUseCase {

    private final TaskRepository repository;

    public CreateTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public TaskResponse execute(Long userId, CreateTaskCommand command) {
        Task task = Task.create(userId, command.title(), command.description());
        return TaskResponse.from(repository.save(task));
    }
}