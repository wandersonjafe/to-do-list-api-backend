package com.wanderson.todolist.application.usecase;

import com.wanderson.todolist.application.dto.TaskResponse;
import com.wanderson.todolist.domain.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ListTaskUseCase {

    private final TaskRepository repository;

    public ListTaskUseCase(TaskRepository repository) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> execute(Long userId) {
        return repository.findAllByOwnerId(userId).stream()
                .map(TaskResponse::from)
                .toList();
    }
}
