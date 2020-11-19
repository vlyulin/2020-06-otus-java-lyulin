package ru.otus.core.dao;

import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    Optional<User> findById(long id);

    Optional<User> findByLogin(String login);

    List<User> findByMask(String id, String name, String login);

    long insertUser(User user);

    void updateUser(User user);

    void insertOrUpdate(User user);

    void deleteById(long userId);

    SessionManager getSessionManager();
}
