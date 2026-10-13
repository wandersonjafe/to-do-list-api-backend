package com.wanderson.todolist.application.usecase;

import com.wanderson.todolist.application.dto.AuthResponse;
import com.wanderson.todolist.application.dto.LoginCommand;
import com.wanderson.todolist.application.port.PasswordHasher;
import com.wanderson.todolist.application.port.TokenGenerator;
import com.wanderson.todolist.domain.exception.InvalidCredentialsException;
import com.wanderson.todolist.domain.model.User;
import com.wanderson.todolist.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class LoginUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public LoginUseCase(UserRepository userRepository,
                        PasswordHasher passwordHasher,
                        TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Transactional(readOnly = true)
    public AuthResponse execute(LoginCommand command) {
        String email = command.email().trim().toLowerCase(Locale.ROOT);

        User user = userRepository.findByEmail(email)
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordHasher.matches(command.password(), user.getPasswordHash())) {
            throw new InvalidCredentialsException();
        }

        return new AuthResponse(tokenGenerator.generate(user.getId(), user.getEmail()), user.getEmail());
    }
}