package ru.otus.frontend.core.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.User;

import java.util.Optional;

public class DbServiceUserImpl implements DBServiceUser {
    private static final Logger logger = LoggerFactory.getLogger(DbServiceUserImpl.class);
    private final UserDao userDao;

    public DbServiceUserImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public long saveUser(User user) {
        try (var sessionManager = userDao.getSessionManager()) {
//            sessionManager.beginSession();
            try {
                var userId = userDao.insertUser(user);
                // В Java борятся за миллисекунды, но допускается делать commit после каждой вставки?
                // Это "дорогая" операция и в базах рекомендуют делать как можно реже и только когда действительно надо
                // При текущем подходе возникает вопрос, как откатить все изменения,
                // если где-то посередине транзакции что-то пошло не так?
//                sessionManager.commitSession();
                logger.info("created user: {}", userId);
                return userId;
            } catch (Exception e) {
//                sessionManager.rollbackSession();
                throw new DbServiceException(e);
            }
        }
    }

    @Override
    public Optional<User> getUser(long id) {
        try (var sessionManager = userDao.getSessionManager()) {
            // Вызов beginSession тут дает непредсказуемость поведения
            // По моему мнению, сессии должны управляться вызывающим методом
//            sessionManager.beginSession();
            try {
                Optional<User> userOptional = userDao.findById(id);
                logger.info("user: {}", userOptional.orElse(null));
                return userOptional;
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
//                sessionManager.rollbackSession();
            }
            return Optional.empty();
        }
    }
}
