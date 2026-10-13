package com.wanderson.todolist.presentation.rest;

import com.wanderson.todolist.application.dto.CreateTaskCommand;
import com.wanderson.todolist.application.dto.TaskResponse;
import com.wanderson.todolist.application.dto.UpdateTaskCommand;
import com.wanderson.todolist.application.usecase.DeleteAllTasksUseCase;
import com.wanderson.todolist.application.usecase.CompleteTaskUseCase;
import com.wanderson.todolist.application.usecase.CreateTaskUseCase;
import com.wanderson.todolist.application.usecase.DeleteTaskUseCase;
import com.wanderson.todolist.application.usecase.ListTaskUseCase;
import com.wanderson.todolist.application.usecase.ReopenTaskUseCase;
import com.wanderson.todolist.application.usecase.UpdateTaskUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final CreateTaskUseCase createTask;
    private final ListTaskUseCase listTasks;
    private final UpdateTaskUseCase updateTask;
    private final CompleteTaskUseCase completeTask;
    private final ReopenTaskUseCase reopenTask;
    private final DeleteTaskUseCase deleteTask;
    private final DeleteAllTasksUseCase deleteAllTasks;

    public TaskController(CreateTaskUseCase createTask,
                          ListTaskUseCase listTasks,
                          UpdateTaskUseCase updateTask,
                          CompleteTaskUseCase completeTask,
                          ReopenTaskUseCase reopenTask,
                          DeleteTaskUseCase deleteTask,
                          DeleteAllTasksUseCase deleteAllTasks) {
        this.createTask = createTask;
        this.listTasks = listTasks;
        this.updateTask = updateTask;
        this.completeTask = completeTask;
        this.reopenTask = reopenTask;
        this.deleteTask = deleteTask;
        this.deleteAllTasks = deleteAllTasks;
    }

    @GetMapping
    public List<TaskResponse> list(@AuthenticationPrincipal Jwt jwt) {
        return listTasks.execute(userId(jwt));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse create(@AuthenticationPrincipal Jwt jwt,
                               @Valid @RequestBody CreateTaskCommand command) {
        return createTask.execute(userId(jwt), command);
    }

    @PutMapping("/{id}")
    public TaskResponse update(@AuthenticationPrincipal Jwt jwt,
                               @PathVariable Long id,
                               @Valid @RequestBody UpdateTaskCommand command) {
        return updateTask.execute(userId(jwt), id, command);
    }

    @PatchMapping("/{id}/complete")
    public TaskResponse complete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return completeTask.execute(userId(jwt), id);
    }

    @PatchMapping("/{id}/reopen")
    public TaskResponse reopen(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        return reopenTask.execute(userId(jwt), id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@AuthenticationPrincipal Jwt jwt, @PathVariable Long id) {
        deleteTask.execute(userId(jwt), id);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAll(@AuthenticationPrincipal Jwt jwt) {
        deleteAllTasks.execute(userId(jwt));
    }

    private Long userId(Jwt jwt) {
        return Long.valueOf(jwt.getSubject());
    }
}