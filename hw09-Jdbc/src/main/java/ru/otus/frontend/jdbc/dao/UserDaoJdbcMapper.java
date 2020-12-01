package ru.otus.frontend.jdbc.dao;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.User;
import ru.otus.frontend.core.sessionmanager.SessionManager;
import ru.otus.frontend.jdbc.mapper.JdbcMapper;
import ru.otus.frontend.jdbc.sessionmanager.SessionManagerJdbc;
import ru.otus.frontend.jdbc.DbExecutorImpl;
import ru.otus.frontend.jdbc.mapper.JdbcMapperImpl;

import java.sql.Connection;
import java.util.Optional;

public class UserDaoJdbcMapper implements UserDao {
    private static final Logger logger = LoggerFactory.getLogger(UserDaoJdbcMapper.class);

    private final SessionManagerJdbc sessionManager;
    private final DbExecutorImpl<User> dbExecutor;
    private final JdbcMapper<User> jdbcMapper;

    public UserDaoJdbcMapper(SessionManagerJdbc sessionManager, DbExecutorImpl<User> dbExecutor) {
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, User.class);
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.of(jdbcMapper.findById(id, User.class));
    }

    @Override
    public long insertUser(User user) {
        return jdbcMapper.insert(user);
    }

    @Override
    public void updateUser(User user) {
        jdbcMapper.update(user);
    }

    @Override
    public void insertOrUpdate(User user) {
        jdbcMapper.insertOrUpdate(user);
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }

    private Connection getConnection() {
        return sessionManager.getCurrentSession().getConnection();
    }
}
