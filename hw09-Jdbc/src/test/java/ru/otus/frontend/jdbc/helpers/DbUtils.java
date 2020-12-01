package ru.otus.frontend.jdbc.helpers;

import org.flywaydb.core.Flyway;
import ru.otus.frontend.h2.DataSourceH2;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class DbUtils {
    public static Connection getConnection(String URL) throws SQLException {
        var connection = DriverManager.getConnection(URL);
        connection.setAutoCommit(false);
        return connection;
    }

    public static void setupDatabase(Connection connection) {
        try {
            DataSource dataSource = mock(DataSourceH2.class);
            given(dataSource.getConnection()).willReturn(connection);
            flywayMigrations(dataSource);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private static void flywayMigrations(DataSource dataSource) {
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
    }
}
