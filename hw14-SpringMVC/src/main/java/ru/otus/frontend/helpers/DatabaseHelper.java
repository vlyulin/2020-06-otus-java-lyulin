package ru.otus.frontend.helpers;

import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.AddressDataSet;
import ru.otus.frontend.core.model.PhoneDataSet;
import ru.otus.frontend.core.model.User;
import ru.otus.frontend.core.sessionmanager.SessionManager;

import java.util.Arrays;
import java.util.HashSet;

public class DatabaseHelper {

    public static void fillDatabase(UserDao userDao) {
        SessionManager sessionManager = userDao.getSessionManager();
        sessionManager.beginSession();

        User user1 = new User(0, "user1", 19, "user1", "user");
        User user2 = new User(0, "user2", 19, "user2", "user");
        User user3 = new User(0, "user3", 19, "user3", "user");
        userDao.insertUser(user1); fillAddresses(user1); fillPhones(user1);
        userDao.insertUser(user2); fillAddresses(user2); fillPhones(user2);
        userDao.insertUser(user3); fillAddresses(user3); fillPhones(user3);

        sessionManager.commitSession();
    }

    private static void fillAddresses( User user ) {
        // Телефоны
        PhoneDataSet phone1 = new PhoneDataSet(user, "+7 (916) 123-12-12");
        PhoneDataSet phone2 = new PhoneDataSet(user, "+7 (916) 444-33-12");
        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));
    }

    private static void fillPhones( User user ) {
        // Адреса
        AddressDataSet address1 = new AddressDataSet(user, "Siktivkar, d.1");
        AddressDataSet address2 = new AddressDataSet(user, "Stalinks, Lenina str. 70");
        user.setAddresses(new HashSet<>(Arrays.asList(address1, address2)));
    }
}
