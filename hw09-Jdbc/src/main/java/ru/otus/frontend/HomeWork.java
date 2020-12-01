package ru.otus.frontend;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.Account;
import ru.otus.frontend.core.model.User;
import ru.otus.frontend.core.service.DbServiceUserImpl;
import ru.otus.frontend.h2.DataSourceH2;
import ru.otus.frontend.jdbc.DbExecutorImpl;
import ru.otus.frontend.jdbc.dao.UserDaoJdbcMapper;
import ru.otus.frontend.jdbc.mapper.JdbcMapper;
import ru.otus.frontend.jdbc.mapper.JdbcMapperImpl;
import ru.otus.frontend.jdbc.sessionmanager.SessionManagerJdbc;

import javax.sql.DataSource;
import java.util.Optional;

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

// Общая часть
        var dataSource = new DataSourceH2();
        flywayMigrations(dataSource);
        var sessionManager = new SessionManagerJdbc(dataSource);

// Работа с пользователем
        DbExecutorImpl<User> dbExecutor = new DbExecutorImpl<>();
        sessionManager.beginSession(); // delete
        JdbcMapper<User> jdbcMapperUser = new JdbcMapperImpl<>(sessionManager, dbExecutor, User.class);
        UserDao userDao = new UserDaoJdbcMapper(sessionManager, dbExecutor);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(new User(0, "dbServiceUser", 15));
        Optional<User> user = dbServiceUser.getUser(id);

        user.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()),
                () -> logger.info("user was not created")
        );
// Работа со счетом
        DbExecutorImpl<Account> dbExecutorAccount = new DbExecutorImpl<>();
        sessionManager.beginSession();
        JdbcMapper<Account> jdbcMapperAccount = new JdbcMapperImpl<Account>(sessionManager, dbExecutorAccount, Account.class);
        Account account = new Account(39456, "Deposit", 1000.00f);
        long accountId = jdbcMapperAccount.insert(account);
        logger.info("Account id = " + accountId);
    }

    private static void flywayMigrations(DataSource dataSource) {
        logger.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        logger.info("db migration finished.");
        logger.info("***");
    }
}
