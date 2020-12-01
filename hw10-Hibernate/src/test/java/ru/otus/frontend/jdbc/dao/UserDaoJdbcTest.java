package ru.otus.frontend.jdbc.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.AddressDataSet;
import ru.otus.frontend.core.model.PhoneDataSet;
import ru.otus.frontend.core.model.User;
import ru.otus.frontend.flyway.MigrationsExecutor;
import ru.otus.frontend.flyway.MigrationsExecutorFlyway;
import ru.otus.frontend.hibernate.HibernateUtils;
import ru.otus.frontend.hibernate.dao.UserDaoHibernate;
import ru.otus.frontend.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.frontend.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.frontend.jdbc.helpers.UserUtils;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoJdbcTest {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    private static final int USER_ID = 100;
    private static final int NULL_USER_ID = 0;
    private static SessionFactory sessionFactory;
    private SessionManagerHibernate sessionManager;

    @BeforeAll
    static void setupDatabase() {
        // Создание базы
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();
        // Получение SessionManager
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class, PhoneDataSet.class, AddressDataSet.class);
    }

    @BeforeEach
    void makeConnection() {
        sessionManager = new SessionManagerHibernate(sessionFactory);
    }

    User getDummyUser(long user_id, String user_name, int age) {
        User user = new User(user_id, user_name, age);
        // Телефоны
        PhoneDataSet phone1 = new PhoneDataSet(user, "+7 (916) 123-12-12");
        PhoneDataSet phone2 = new PhoneDataSet(user, "+7 (916) 444-33-12");
        user.setPhones(new HashSet<>(Arrays.asList(phone1, phone2)));
        // Адреса
        AddressDataSet address1 = new AddressDataSet(user, "Siktivkar, d.1");
        AddressDataSet address2 = new AddressDataSet(user, "Stalinks, Lenina str. 70");
        user.setAddresses(new HashSet<>(Arrays.asList(address1, address2)));
        return user;
    }

    @Test
    void findById() {
        sessionManager.beginSession();

        // В базе пользователя нет. Должна быть вставка нового пользователя
        User userForInsert = getDummyUser(USER_ID, "TestUser", 34);
        DatabaseSessionHibernate databaseSessionHibernate = sessionManager.getCurrentSession();
        Session session = databaseSessionHibernate.getHibernateSession();
        final long[] insertedUserId = new long[1]; // Не возвращает id
        session.doWork(connection -> {
            insertedUserId[0] = UserUtils.insertUser(connection, userForInsert);
        });

        UserDao userDao = new UserDaoHibernate(sessionManager);
        // User foundedUser = userDao.findById(insertedUserId[0]).get();
        User foundedUser = userDao.findById(USER_ID).get();
        assertThat(foundedUser).isEqualTo(userForInsert);

        sessionManager.rollbackSession();
    }

    @Test
    void insertUser() {
        sessionManager.beginSession();

        User user = getDummyUser(NULL_USER_ID, "TestUser", 34);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        userDao.insertUser(user);

        DatabaseSessionHibernate databaseSessionHibernate = sessionManager.getCurrentSession();
        Session session = databaseSessionHibernate.getHibernateSession();
        session.flush();

        session.doWork(connection -> {
            User dbUser = UserUtils.selectUser(connection, user.getId());
            assertThat(user).isEqualTo(dbUser);
        });
        sessionManager.rollbackSession();
    }

    @Test
    void updateUser() {
        sessionManager.beginSession();

        DatabaseSessionHibernate databaseSessionHibernate = sessionManager.getCurrentSession();
        Session session = databaseSessionHibernate.getHibernateSession();

        UserDao userDao = new UserDaoHibernate(sessionManager);
        User user = getDummyUser(NULL_USER_ID, "TestUser", 34);
        userDao.insertUser(user);

        User userForUpdate = getDummyUser(user.getId(), "Updated user", 36);
        userDao.updateUser(userForUpdate);
        session.flush();

        session.doWork(connection -> {
            User updatedUser = UserUtils.selectUser(connection, user.getId());
            assertThat(updatedUser).isEqualTo(userForUpdate);
        });
        sessionManager.rollbackSession();
    }

    @Test
    void insertOrUpdate() {
        sessionManager.beginSession();
        UserDao userDao = new UserDaoHibernate(sessionManager);
        // В базе пользователя нет. Должна быть вставка нового пользователя
        User user = getDummyUser(NULL_USER_ID, "User for insert", 27);
        userDao.insertOrUpdate(user);

        DatabaseSessionHibernate databaseSessionHibernate = sessionManager.getCurrentSession();
        Session session = databaseSessionHibernate.getHibernateSession();
        session.flush();

        session.doWork(connection -> {
            User insertedUser = UserUtils.selectUser(connection, user.getId());
            assertThat(insertedUser).isEqualTo(user);
        });

        // В базе пользователь уже есть, должен произойти update
        user.setName("Updated user for insert");
        userDao.insertOrUpdate(user);
        session.flush();

        session.doWork(connection -> {
            User updatedUser = UserUtils.selectUser(connection, user.getId());
            assertThat(updatedUser).isEqualTo(user);
        });
        sessionManager.rollbackSession();
    }
}