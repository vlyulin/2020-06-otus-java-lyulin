package ru.otus.frontend;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.AddressDataSet;
import ru.otus.frontend.core.model.PhoneDataSet;
import ru.otus.frontend.core.model.User;
import ru.otus.frontend.core.service.DbServiceUserImpl;
import ru.otus.frontend.flyway.MigrationsExecutor;
import ru.otus.frontend.flyway.MigrationsExecutorFlyway;
import ru.otus.frontend.hibernate.HibernateUtils;
import ru.otus.frontend.hibernate.dao.UserDaoHibernate;
import ru.otus.frontend.hibernate.sessionmanager.SessionManagerHibernate;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;

public class HomeWork {
    private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) {

// Общая часть
        // Создание базы
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();
        // Получение SessionManager
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class, PhoneDataSet.class, AddressDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);

// Работа с пользователем
        User user = new User(0, "dbServiceUser", 15);
        // Телефоны
        PhoneDataSet phone1 = new PhoneDataSet(user, "+7 (916) 123-12-12");
        PhoneDataSet phone2 = new PhoneDataSet(user, "+7 (916) 444-33-12");
        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));
        // Адреса
        AddressDataSet address1 = new AddressDataSet(user, "Siktivkar, d.1");
        AddressDataSet address2 = new AddressDataSet(user, "Stalinks, Lenina str. 70");
        user.setAddresses(new HashSet<>(Arrays.asList(address1, address2)));

        UserDao userDao = new UserDaoHibernate(sessionManager);

// Код дальше должен остаться, т.е. userDao должен использоваться
        var dbServiceUser = new DbServiceUserImpl(userDao);
        var id = dbServiceUser.saveUser(user);
        Optional<User> optUser = dbServiceUser.getUser(id);

        optUser.ifPresentOrElse(
                crUser -> logger.info("created user, name:{}", crUser.getName()
                        + "; phones: " + crUser.getPhones().toString()
                        + "; addresses: " + crUser.getAddresses().toString()
                ),
                () -> logger.info("user was not created")
        );
    }
}
