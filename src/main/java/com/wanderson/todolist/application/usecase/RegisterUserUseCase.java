package com.wanderson.todolist.application.usecase;

import com.wanderson.todolist.application.dto.AuthResponse;
import com.wanderson.todolist.application.dto.RegisterCommand;
import com.wanderson.todolist.application.port.PasswordHasher;
import com.wanderson.todolist.application.port.TokenGenerator;
import com.wanderson.todolist.domain.exception.EmailAlreadyInUseException;
import com.wanderson.todolist.domain.model.User;
import com.wanderson.todolist.domain.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordHasher passwordHasher;
    private final TokenGenerator tokenGenerator;

    public RegisterUserUseCase(UserRepository userRepository,
                               PasswordHasher passwordHasher,
                               TokenGenerator tokenGenerator) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
        this.tokenGenerator = tokenGenerator;
    }

    @Transactional
    public AuthResponse execute(RegisterCommand command) {
        String email = command.email().trim().toLowerCase(Locale.ROOT);

        if (userRepository.existsByEmail(email)) {
            throw new EmailAlreadyInUseException();
        }

        User user = User.create(email, passwordHasher.hash(command.password()));
        User saved = userRepository.save(user);

        return new AuthResponse(tokenGenerator.generate(saved.getId(), saved.getEmail()), saved.getEmail());
    }
}