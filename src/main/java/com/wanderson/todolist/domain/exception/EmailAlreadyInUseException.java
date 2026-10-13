package com.wanderson.todolist.domain.exception;

public class EmailAlreadyInUseException extends RuntimeException {

    public EmailAlreadyInUseException() {
        super("Este e-mail já está em uso");
    }
}