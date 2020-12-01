package ru.otus.frontend.jdbc.helpers;

import ru.otus.frontend.core.model.AddressDataSet;
import ru.otus.frontend.core.model.PhoneDataSet;
import ru.otus.frontend.core.model.User;

import java.sql.*;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

public class UserUtils {
    public static User selectUser(Connection connection, long id) throws SQLException {

        Set<PhoneDataSet> phones = selectPhones(connection, id);
        Set<AddressDataSet> addresses = selectAddresses(connection, id);

        User user = executeSelect(connection, "select id, name, age from users where id  = ?",
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
        phones.stream().forEach(elem -> elem.setUser(user));
        addresses.stream().forEach(elem -> elem.setUser(user));
        user.setPhones(phones);
        user.setAddresses(addresses);
        return user;
    }

    private static Set<PhoneDataSet> selectPhones(Connection connection, long user_id) throws SQLException {
        return (Set<PhoneDataSet>) executeUniversalSelect(connection, "select id, phone_number from phones where user_id  = ?",
                user_id, rs -> {
                    Set<PhoneDataSet> phones = new HashSet<>();
                    try {
                        if (rs.next()) {
                            PhoneDataSet phone = new PhoneDataSet();
                            phone.setId(rs.getLong("id"));
                            phone.setNumber(rs.getString("phone_number"));
                            phones.add(phone);
                        }
                        return phones;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }).get();
    }

    private static Set<AddressDataSet> selectAddresses(Connection connection, long user_id) throws SQLException {
        return (Set<AddressDataSet>) executeUniversalSelect(connection, "select id, street from addresses where user_id  = ?",
                user_id, rs -> {
                    Set<AddressDataSet> streets = new HashSet<>();
                    try {
                        if (rs.next()) {
                            AddressDataSet street = new AddressDataSet();
                            street.setId(rs.getLong("id"));
                            street.setStreet(rs.getString("street"));
                            streets.add(street);
                        }
                        return streets;
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                    return null;
                }).get();
    }

    private static Optional<User> executeSelect(Connection connection, String sql, Object id,
                                                Function<ResultSet, User> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setObject(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    private static Optional<Object> executeUniversalSelect(Connection connection, String sql, Object id,
                                                           Function<ResultSet, Object> rsHandler) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(sql)) {
            pst.setObject(1, id);
            try (ResultSet rs = pst.executeQuery()) {
                return Optional.ofNullable(rsHandler.apply(rs));
            }
        }
    }

    public static long insertUser(Connection connection, User user) throws SQLException {
        if (user == null) {
            throw new RuntimeException("User is null");
        }

        for (PhoneDataSet phone : user.getPhones()) {
            insertPhone(connection, phone.getId(), user.getId(), phone.getNumber());
        }
        for (AddressDataSet address : user.getAddresses()) {
            insertAddress(connection, address.getId(), user.getId(), address.getStreet());
        }
        return insertRecord(connection, user.getId(), user.getName(), user.getAge(), user.getLogin(), user.getPassword());
    }

    private static long insertRecord(Connection connection, long id, String name, int age, String login, String password) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(
                "insert into USERS(id, name, age, login, password) values (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            pst.setLong(1, id);
            pst.setString(2, name);
            pst.setInt(3, age);
            pst.setString(4, login);
            pst.setString(5, password);

            int rowCount = pst.executeUpdate(); //Блокирующий вызов
            if (rowCount == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            try (ResultSet rs = pst.getGeneratedKeys()) {
                while (rs.next()) {
//                    connection.commit();
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }

    private static long insertPhone(Connection connection, long id, long user_id, String phone_number) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(
                "insert into Phones(id, user_id, phone_number) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            pst.setLong(1, id);
            pst.setLong(2, user_id);
            pst.setString(3, phone_number);
            int rowCount = pst.executeUpdate(); //Блокирующий вызов
            if (rowCount == 0) {
                throw new SQLException("Creating phone failed, no rows affected.");
            }
            try (ResultSet rs = pst.getGeneratedKeys()) {
                if(rs.next()) {
                    System.out.println("Next");
                }
                while (rs.next()) {
                    connection.commit();
                    return rs.getLong(1);
                }
            }
        }
        return -1;
    }

    private static long insertAddress(Connection connection, long id, long user_id, String street) throws SQLException {
        try (PreparedStatement pst = connection.prepareStatement(
                "insert into Addresses(id, user_id, street) values (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            pst.setLong(1, id);
            pst.setLong(2, user_id);
            pst.setString(3, street);
            int rowCount = pst.executeUpdate(); //Блокирующий вызов
            if (rowCount == 0) {
                throw new SQLException("Creating address failed, no rows affected.");
            }
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
