package ru.otus.frontend.jdbc.dao;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.User;
import ru.otus.frontend.jdbc.helpers.DbUtils;
import ru.otus.frontend.jdbc.helpers.UserUtils;
import ru.otus.frontend.jdbc.DbExecutorImpl;
import ru.otus.frontend.jdbc.sessionmanager.DatabaseSessionJdbc;
import ru.otus.frontend.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class UserDaoJdbcTest {
    private static final String URL = "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1";
    public static final String INSERT_TEST_ROLLBACK = "InsertTest";
    public static final int USER_ID = 100;
    private DataSource dataSource;
    private Connection connection;
    private SessionManagerJdbc sessionManagerJdbc;
    private DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
    private DatabaseSessionJdbc databaseSessionJdbc;

    @BeforeAll
    static void setupDatabase() throws SQLException {
        DbUtils.setupDatabase(DbUtils.getConnection(URL));
    }

    @BeforeEach
    void makeConnection() throws SQLException {

        this.connection = DbUtils.getConnection(URL);
        connection.setAutoCommit(false);

        databaseSessionJdbc = mock(DatabaseSessionJdbc.class);
        given(databaseSessionJdbc.getConnection()).willReturn(connection);

        sessionManagerJdbc = mock(SessionManagerJdbc.class);
        given(sessionManagerJdbc.getCurrentSession()).willReturn(databaseSessionJdbc);
    }

    @Test
    void findById() throws SQLException {
        UserDao userDao = new UserDaoJdbcMapper(sessionManagerJdbc, dbExecutor);
        // В базе пользователя нет. Должна быть вставка нового пользователя
        User userForInsert = new User(USER_ID, "User for insert", 27);
        long id = userDao.insertUser(userForInsert);
        User insertedUser = UserUtils.selectUser(connection, USER_ID);
        connection.rollback();
        assertThat(insertedUser).isEqualTo(userForInsert);
    }

    @Test
    void insertUser() throws SQLException {
        User user = new User(USER_ID, "TestUser", 16);
        UserDao userDao = new UserDaoJdbcMapper(sessionManagerJdbc, dbExecutor);
        userDao.insertUser(user);
        User dbUser = UserUtils.selectUser(connection,USER_ID);
        connection.rollback();
        assertThat(user).isEqualTo(dbUser);
    }

    @Test
    void updateUser() throws SQLException {
        User user = new User(USER_ID, "TestUser", 16);
        UserDao userDao = new UserDaoJdbcMapper(sessionManagerJdbc, dbExecutor);
        long insertedUserId = UserUtils.insertUser(connection, user);
        User userForUpdate = new User(USER_ID, "Updated user", 36);
        userDao.updateUser(userForUpdate);
        User updatedUser = UserUtils.selectUser(connection, USER_ID);
        connection.rollback();
        assertThat(updatedUser).isEqualTo(userForUpdate);
    }

    @Test
    void insertOrUpdate() throws SQLException {
        UserDao userDao = new UserDaoJdbcMapper(sessionManagerJdbc, dbExecutor);
        // В базе пользователя нет. Должна быть вставка нового пользователя
        User userForInsert = new User(USER_ID, "User for insert", 27);
        userDao.insertOrUpdate(userForInsert);

        User insertedUser = UserUtils.selectUser(connection, USER_ID);
        assertThat(insertedUser).isEqualTo(userForInsert);

        // В базе пользователь уже есть, должен произойти update
        User userForUpdate = new User(USER_ID, "Updated user for insert", 27);
        userDao.insertOrUpdate(userForUpdate);

        User updatedUser = UserUtils.selectUser(connection, USER_ID);
        assertThat(updatedUser).isEqualTo(userForUpdate);

        connection.rollback();
    }
}