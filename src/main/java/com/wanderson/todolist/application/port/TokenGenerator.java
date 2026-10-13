package com.wanderson.todolist.application.port;

public interface TokenGenerator {

    String generate(Long userId, String email);
}