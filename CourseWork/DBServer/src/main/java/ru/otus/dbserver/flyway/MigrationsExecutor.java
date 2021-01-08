package ru.otus.dbserver.flyway;

public interface MigrationsExecutor {
    void cleanDb();
    void executeMigrations();
}
