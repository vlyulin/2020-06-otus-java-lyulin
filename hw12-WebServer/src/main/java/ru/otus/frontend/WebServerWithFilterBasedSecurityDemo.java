package ru.otus.frontend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.SessionFactory;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.AddressDataSet;
import ru.otus.frontend.core.model.PhoneDataSet;
import ru.otus.frontend.core.model.User;
import ru.otus.frontend.core.sessionmanager.SessionManager;
import ru.otus.frontend.flyway.MigrationsExecutor;
import ru.otus.frontend.flyway.MigrationsExecutorFlyway;
import ru.otus.frontend.hibernate.sessionmanager.SessionManagerHibernate;
import ru.otus.frontend.server.UsersWebServer;
import ru.otus.frontend.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.frontend.services.UserAuthService;
import ru.otus.frontend.services.UserAuthServiceImpl;
import ru.otus.frontend.hibernate.HibernateUtils;
import ru.otus.frontend.hibernate.dao.UserDaoHibernate;
import ru.otus.frontend.services.TemplateProcessor;
import ru.otus.frontend.services.TemplateProcessorImpl;

import java.util.Arrays;
import java.util.HashSet;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {
        // Создание базы
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();
        // Получение SessionManager
        SessionFactory sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class, PhoneDataSet.class, AddressDataSet.class);
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        UserDao userDao = new UserDaoHibernate(sessionManager);
        fillDatabase(userDao);

        userDao.getSessionManager().beginSession();

        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        UserAuthService authService = new UserAuthServiceImpl(userDao);

        UsersWebServer usersWebServer = new UsersWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, userDao, gson, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }

    private static void fillDatabase(UserDao userDao) {
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
