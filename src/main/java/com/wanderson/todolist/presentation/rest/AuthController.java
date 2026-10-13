package com.wanderson.todolist.presentation.rest;

import com.wanderson.todolist.application.dto.AuthResponse;
import com.wanderson.todolist.application.dto.LoginCommand;
import com.wanderson.todolist.application.dto.RegisterCommand;
import com.wanderson.todolist.application.usecase.LoginUseCase;
import com.wanderson.todolist.application.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final RegisterUserUseCase registerUser;
    private final LoginUseCase login;

    public AuthController(RegisterUserUseCase registerUser, LoginUseCase login) {
        this.registerUser = registerUser;
        this.login = login;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody RegisterCommand command) {
        return registerUser.execute(command);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody LoginCommand command) {
        return login.execute(command);
    }
}
