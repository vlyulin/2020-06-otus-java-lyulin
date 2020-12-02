package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.Account;
import ru.otus.jdbc.helpers.AccountUtils;
import ru.otus.jdbc.helpers.DbUtils;
import ru.otus.jdbc.DbExecutorImpl;
import ru.otus.jdbc.sessionmanager.DatabaseSessionJdbc;
import ru.otus.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class JdbcAccountMapperImplTest {

    private static final String URL = "jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1";
    public static final String INSERT_TEST_ROLLBACK = "InsertTest";
    public static final int ACCOUNT_ID = 32789;
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
    @DisplayName("Тестирование вставки Account (Insert)")
    void insert() throws SQLException {

        Account account = new Account(ACCOUNT_ID, "TestAccount", 16);

        DbExecutorImpl<Account> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, Account.class);

        jdbcMapperAccount.insert(account);
        Account dbAccount = AccountUtils.selectAccount(connection, ACCOUNT_ID);
        connection.rollback();

        assertThat(account).isEqualTo(dbAccount);
    }

    @Test
    @DisplayName("Тестирование изменение Account (Update)")
    void update() throws SQLException {
        Account account = new Account(ACCOUNT_ID, "TestAccount", 16);

        DbExecutorImpl<Account> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, Account.class);

        long insertedAccountId = AccountUtils.insertAccount(connection, account);

        Account accountForUpdate = new Account(ACCOUNT_ID, "Updated account", 36);
        jdbcMapperAccount.update(accountForUpdate);

        Account updatedAccount = AccountUtils.selectAccount(connection, ACCOUNT_ID);
        connection.rollback();

        assertThat(updatedAccount).isEqualTo(accountForUpdate);
    }

    @Test
    @DisplayName("Тестирование изменения или вставки Account (InsertOrUpdate)")
    void insertOrUpdate() throws SQLException {
        DbExecutorImpl<Account> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, Account.class);

        // В базе пользователя нет. Должна быть вставка нового пользователя
        Account accountForInsert = new Account(ACCOUNT_ID, "Account for insert", 27);
        jdbcMapperAccount.insertOrUpdate(accountForInsert);

        Account insertedAccount = AccountUtils.selectAccount(connection, ACCOUNT_ID);
        assertThat(insertedAccount).isEqualTo(accountForInsert);

        // В базе пользователь уже есть, должен произойти update
        Account accountForUpdate = new Account(ACCOUNT_ID, "Updated account for insert", 27);
        jdbcMapperAccount.insertOrUpdate(accountForUpdate);

        Account updatedAccount = AccountUtils.selectAccount(connection, ACCOUNT_ID);
        assertThat(updatedAccount).isEqualTo(accountForUpdate);

        connection.rollback();
    }

    @Test
    @DisplayName("Тестирование поиска Account по No")
    void findById() throws SQLException {
        DbExecutorImpl<Account> dbExecutor = new DbExecutorImpl<>();
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<>(sessionManagerJdbc, dbExecutor, Account.class);

        // В базе пользователя нет. Должна быть вставка нового пользователя
        Account accountForInsert = new Account(ACCOUNT_ID, "Account for insert", 27);
        long id = AccountUtils.insertAccount(connection, accountForInsert);

        Account insertedAccount = AccountUtils.selectAccount(connection, ACCOUNT_ID);
        connection.rollback();
        assertThat(insertedAccount).isEqualTo(accountForInsert);
    }
}