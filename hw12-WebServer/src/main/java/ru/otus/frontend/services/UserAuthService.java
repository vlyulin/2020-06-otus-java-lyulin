package ru.otus.frontend.services;

public interface UserAuthService {
    boolean authenticate(String login, String password);
}
