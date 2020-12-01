package ru.otus.frontend.flyway;

public interface MigrationsExecutor {
    void cleanDb();
    void executeMigrations();
}
