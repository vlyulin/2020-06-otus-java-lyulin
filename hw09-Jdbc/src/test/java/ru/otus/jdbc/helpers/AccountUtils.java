package ru.otus.jdbc.helpers;

import ru.otus.core.model.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.function.Function;

public class AccountUtils {
    public static Account selectAccount(Connection connection, long id) throws SQLException {
        return executeSelect(connection, "select no, type, rest from account where no  = ?",
                id, rs -> {
                    try {
                        if (rs.next()) {
                            return new Account(
                                    rs.getLong("no"), 
                                    rs.getString("type"), 
                                    rs.getFloat("rest"));
                        }
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }).get();
    }

    private static Optional<Account> executeSelect(Connection connection, String sql, Object id,
                                         Function<ResultSet, Account> rsHandler) throws SQLException {
        try (var pst = connection.prepareStatement(sql)) {
            pst.setObject(1, id);
            try (var rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    public static long insertAccount(Connection connection,Account account) throws SQLException {
        return insertRecord(connection, account.getNo(), account.getType(), account.getRest());
    }

    private static long insertRecord(Connection connection, long no, String type, float rest) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(
                "insert into ACCOUNT(no, type, rest) values (?, ?, ?)")) {
            pst.setLong(1, no);
            pst.setString(2, type);
            pst.setFloat(3, rest);
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
