package ru.otus.dbserver.configurations;

import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.dbserver.flyway.MigrationsExecutor;
import ru.otus.dbserver.flyway.MigrationsExecutorFlyway;
import ru.otus.dbserver.helpers.DatabaseHelper;

// https://www.baeldung.com/running-setup-logic-on-startup-in-spring
public class InitDatabase {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private UserDao userDao;

    public InitDatabase(UserDao userDao) {
        this.userDao = userDao;
    }

    public void init() {
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();
        DatabaseHelper.fillDatabase(userDao);
        userDao.getSessionManager().beginSession();
    }
}
