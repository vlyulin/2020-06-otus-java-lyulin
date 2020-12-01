package ru.otus.frontend.configurations;

import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.flyway.MigrationsExecutor;
import ru.otus.frontend.flyway.MigrationsExecutorFlyway;
import ru.otus.frontend.helpers.DatabaseHelper;

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
