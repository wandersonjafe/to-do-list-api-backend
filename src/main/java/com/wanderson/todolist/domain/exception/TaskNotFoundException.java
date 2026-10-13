package com.wanderson.todolist.domain.exception;

public class TaskNotFoundException extends RuntimeException {

    public TaskNotFoundException(Long id) {
        super ("Tarefa não encontrada" + id);
    }
}
