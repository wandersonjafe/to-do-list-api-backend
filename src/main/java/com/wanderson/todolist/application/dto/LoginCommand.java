package com.wanderson.todolist.application.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginCommand(
        @NotBlank(message = "O e-mail é obrigatório") String email,
        @NotBlank(message = "A senha é obrigatória") String password
) {}