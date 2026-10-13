package com.wanderson.todolist.presentation.rest;

import java.util.Map;

public record ErrorResponse(int status, String message, Map<String, String> fieldErrors) {

    public static ErrorResponse of(int status, String message) {
        return new ErrorResponse(status, message, null);
    }

}
