package ru.otus.dbserver.core.service;


import ru.otus.common.core.model.User;

import java.util.Optional;

public interface DBServiceUser {

    long saveUser(User user);

    Optional<User> getUser(long id);
}
