package ru.otus.frontend.core.dao;

import java.util.Optional;

import ru.otus.frontend.core.model.User;
import ru.otus.frontend.core.sessionmanager.SessionManager;

public interface UserDao {
    Optional<User> findById(long id);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    SessionManager getSessionManager();
}
