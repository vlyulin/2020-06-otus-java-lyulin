package ru.otus.frontend.core.service;

import ru.otus.frontend.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}
