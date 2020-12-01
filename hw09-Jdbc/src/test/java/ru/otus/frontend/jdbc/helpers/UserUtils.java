package ru.otus.frontend.jdbc.helpers;

import ru.otus.frontend.core.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public class UserUtils {
    public static User selectUser(Connection connection, long id) throws SQLException {
        return executeSelect(connection, "select id, name, age from user where id  = ?",
                id, rs -> {
                    try {
                        if (rs.next()) {
                            return new User(rs.getLong("id"), rs.getString("name"), rs.getInt("age"));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }).get();
    }

    private static Optional<User> executeSelect(Connection connection, String sql, Object id,
                                         Function<ResultSet, User> rsHandler) throws SQLException {
        try (var pst = connection.prepareStatement(sql)) {
            pst.setObject(1, id);
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    public static long insertUser(Connection connection,User user) throws SQLException {
        return insertRecord(connection, user.getId(), user.getName(), user.getAge());
    }

    private static long insertRecord(Connection connection, long id, String name, int age) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement("insert into USER(id, name, age) values (?, ?, ?)")) {
            pst.setLong(1, id);
            pst.setString(2, name);
            pst.setInt(3, age);
            // TODO: Не возвращает Id
            int rowCount = pst.executeUpdate(); //Блокирующий вызов
            try (ResultSet rs = pst.getGeneratedKeys()) {
                while (rs.next()) {
                    connection.commit();
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }
}
