package ru.otus.frontend.performance;

import org.hibernate.SessionFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ro.otus.common.core.model.AddressDataSet;
import ro.otus.common.core.model.PhoneDataSet;
import ro.otus.common.core.model.User;
import ru.otus.dbserver.cache.HwCache;
import ru.otus.dbserver.cache.MyCache;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.dbserver.core.service.CachedDBServiceUserImpl;
import ru.otus.dbserver.core.service.DBServiceUser;
import ru.otus.dbserver.core.service.DbServiceUserImpl;
import ru.otus.dbserver.flyway.MigrationsExecutor;
import ru.otus.dbserver.flyway.MigrationsExecutorFlyway;
import ru.otus.dbserver.hibernate.HibernateUtils;
import ru.otus.dbserver.hibernate.dao.UserDaoHibernate;
import ru.otus.dbserver.hibernate.sessionmanager.SessionManagerHibernate;

import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class PerformanceTest {

    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final String USER_NAME = "User #";
    public static final int TOTAL_USERS = 1000;
    public static final int TESTS_NUMBER = 100;
    private static SessionFactory sessionFactory;

    private static final Logger logger = (Logger) LoggerFactory.getLogger(PerformanceTest.class);

    @BeforeAll
    static void setupDatabase() {
        // Создание базы
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();
        // Получение SessionManager
        sessionFactory = HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class, PhoneDataSet.class, AddressDataSet.class);
    }

    @Test
    @DisplayName("Тестирование производительности")
    void performanceTest() {
        SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
        sessionManager.beginSession();

        // Подготовка
        UserDao userDao = new UserDaoHibernate(sessionManager);
        DBServiceUser dbServiceUser = new DbServiceUserImpl(userDao);
        HwCache<String, User> hwCache = new MyCache<>(TOTAL_USERS);
        HwCache<String,User> spyHwCache = spy(hwCache);
        DBServiceUser cachedDbServiceUser = new CachedDBServiceUserImpl(dbServiceUser, spyHwCache);

        // заполняем базу данных тестовыми пользователями через cachedDbServiceUser, для заполнения кэша
        long[] ids = new long[TOTAL_USERS];
        fillDatabase(cachedDbServiceUser, TOTAL_USERS, ids);

        // Получаем время выполнения произвольного поиска TESTS_NUMBER пользователей без кэша
        logger.info("");
        logger.info("Поиск " + TESTS_NUMBER + " пользователей без кэша.");
        Instant start = Instant.now();
        for(int cnt = 0; cnt < TESTS_NUMBER; cnt++) {
            int idx = ThreadLocalRandom.current().nextInt(0, TOTAL_USERS);
            Optional<User> user = dbServiceUser.getUser(ids[idx]);
        }
        // Вычисляем время выполнения
        Instant end = Instant.now();
        Duration timeElapsedWithoutCache = Duration.between(start, end);

        // Получаем время выполнения произвольного поиска TESTS_NUMBER пользователей c кэшем
        logger.info("");
        logger.info("Поиск " + TESTS_NUMBER + " пользователей c кэшем.");
        start = Instant.now();
        for(int cnt = 0; cnt < TESTS_NUMBER; cnt++) {
            int idx = ThreadLocalRandom.current().nextInt(0, TOTAL_USERS);
            Optional<User> user = cachedDbServiceUser.getUser(ids[idx]);
        }
        // Вычисляем время выполнения
        end = Instant.now();
        Duration timeElapsedWithCache = Duration.between(start, end);

        logger.info("Time comparison:" +
                " without cache: " + timeElapsedWithoutCache.toMillis() +
                " with cache: " + timeElapsedWithCache.toMillis() +" milliseconds.");

        // В надежде, что база останется пустая
        sessionManager.rollbackSession();

        // Проверка, что кэш дает прирост производительности
        verify(spyHwCache, times(TESTS_NUMBER)).get(anyString());
        assertThat(timeElapsedWithCache).isLessThan(timeElapsedWithoutCache);
    }

    private void fillDatabase(DBServiceUser dbServiceUser, int count, long[] ids ) {
        logger.info("Заполнение базы тестовыми пользователями");
        for(int idx = 0; idx < count; idx++ ) {
            String userName = USER_NAME + idx;
            User nextUser = getDummyUser(0,
                    userName,
                    ThreadLocalRandom.current().nextInt(16, 120 + 1),
                    userName,
                    userName);
            ids[idx] = dbServiceUser.saveUser(nextUser);
        }
        logger.info("Завершение заполнения базы тестовыми пользователями.");
    }
    
    private User getDummyUser(long user_id, String user_name, int age, String login, String password) {
        User user = new User(user_id, user_name, age, login, password);
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

}
