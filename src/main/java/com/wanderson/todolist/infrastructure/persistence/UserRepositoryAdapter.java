package com.wanderson.todolist.infrastructure.persistence;

import com.wanderson.todolist.domain.model.User;
import com.wanderson.todolist.domain.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaRepository;

    public UserRepositoryAdapter(JpaUserRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User save(User user) {
        UserEntity saved = jpaRepository.save(toEntity(user));
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        return new UserEntity(user.getId(), user.getEmail(), user.getPasswordHash(), user.getCreatedAt());
    }

    private User toDomain(UserEntity entity) {
        return User.restore(entity.getId(), entity.getEmail(), entity.getPasswordHash(), entity.getCreatedAt());
    }
}