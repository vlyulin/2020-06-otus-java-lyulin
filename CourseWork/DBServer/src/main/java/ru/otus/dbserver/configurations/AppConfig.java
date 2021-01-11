package ru.otus.dbserver.configurations;

import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.common.core.model.AddressDataSet;
import ru.otus.common.core.model.PhoneDataSet;
import ru.otus.common.core.model.User;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.dbserver.flyway.MigrationsExecutor;
import ru.otus.dbserver.flyway.MigrationsExecutorFlyway;
import ru.otus.dbserver.hibernate.HibernateUtils;
import ru.otus.dbserver.hibernate.dao.UserDaoHibernate;
import ru.otus.dbserver.hibernate.sessionmanager.SessionManagerHibernate;

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
