package ru.otus.configurations;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.core.dao.UserDao;
import ru.otus.core.model.AddressDataSet;
import ru.otus.core.model.PhoneDataSet;
import ru.otus.core.model.User;
import ru.otus.flyway.MigrationsExecutor;
import ru.otus.flyway.MigrationsExecutorFlyway;
import ru.otus.hibernate.HibernateUtils;
import ru.otus.hibernate.dao.UserDaoHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

// https://www.baeldung.com/tomcat-deploy-war
// https://flywaydb.org/documentation/usage/gradle/
// https://reflectoring.io/database-migration-spring-boot-flyway/
@Configuration
public class AppConfig {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    @Bean
    public SessionFactory getSessionFactory() {
        MigrationsExecutor migrationsExecutor = new MigrationsExecutorFlyway(HIBERNATE_CFG_FILE);
        migrationsExecutor.executeMigrations();

        return HibernateUtils.buildSessionFactory(HIBERNATE_CFG_FILE, User.class, PhoneDataSet.class, AddressDataSet.class);
    }

    @Bean
    public SessionManagerHibernate getSessionManagerHibernate(SessionFactory sessionFactory) {
        return new SessionManagerHibernate(sessionFactory);
    }

    @Bean
    public UserDao getUserDao(SessionManagerHibernate sessionManager) {
        UserDao userDao = new UserDaoHibernate(sessionManager);
        return userDao;
    }

    // https://www.baeldung.com/running-setup-logic-on-startup-in-spring
    @Bean(initMethod="init")
    public InitDatabase InitBean(UserDao userDao) {
        return new InitDatabase(userDao);
    }
}
