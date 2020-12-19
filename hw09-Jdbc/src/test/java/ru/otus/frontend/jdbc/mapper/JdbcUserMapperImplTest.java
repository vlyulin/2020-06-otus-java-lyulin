package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.User;
import ru.otus.jdbc.helpers.DbUtils;
import ru.otus.jdbc.helpers.UserUtils;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.DatabaseSessionJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JdbcUserMapperImplTest {

    private static final String URL = "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1";
    public static final String INSERT_TEST_ROLLBACK = "InsertTest";
    public static final int USER_ID = 100;
    private DataSource dataSource;
    private Connection connection;
    private SessionManagerJdbc sessionManagerJdbc;
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
    @DisplayName("Тестирование вставки пользователя (Insert)")
    void insert() throws SQLException {

        User user = new User(USER_ID, "TestUser", 16);

        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, User.class);

        jdbcMapperUser.insert(user);
        User dbUser = UserUtils.selectUser(connection,USER_ID);
        connection.rollback();

        assertThat(user).isEqualTo(dbUser);
    }

    @Test
    @DisplayName("Тестирование изменение пользователя (Update)")
    void update() throws SQLException {
        User user = new User(USER_ID, "TestUser", 16);

        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, User.class);

        long insertedUserId = UserUtils.insertUser(connection, user);

        User userForUpdate = new User(USER_ID, "Updated user", 36);
        jdbcMapperUser.update(userForUpdate);

        User updatedUser = UserUtils.selectUser(connection, USER_ID);
        connection.rollback();

        assertThat(updatedUser).isEqualTo(userForUpdate);
    }

    @Test
    @DisplayName("Тестирование изменения или вставки пользователя (InsertOrUpdate)")
    void insertOrUpdate() throws SQLException {
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, User.class);

        // В базе пользователя нет. Должна быть вставка нового пользователя
        User userForInsert = new User(USER_ID, "User for insert", 27);
        jdbcMapperUser.insertOrUpdate(userForInsert);

        User insertedUser = UserUtils.selectUser(connection, USER_ID);
        assertThat(insertedUser).isEqualTo(userForInsert);

        // В базе пользователь уже есть, должен произойти update
        User userForUpdate = new User(USER_ID, "Updated user for insert", 27);
        jdbcMapperUser.insertOrUpdate(userForUpdate);

        User updatedUser = UserUtils.selectUser(connection, USER_ID);
        assertThat(updatedUser).isEqualTo(userForUpdate);

        connection.rollback();
    }

    @Test
    @DisplayName("Тестирование поиска пользователя по Id")
    void findById() throws SQLException {
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, User.class);

        // В базе пользователя нет. Должна быть вставка нового пользователя
        User userForInsert = new User(USER_ID, "User for insert", 27);
        long id = UserUtils.insertUser(connection, userForInsert);

        User insertedUser = UserUtils.selectUser(connection, USER_ID);
        connection.rollback();
        assertThat(insertedUser).isEqualTo(userForInsert);
    }
}