package ru.otus.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.dao.UserDao;
import ru.otus.core.dao.UserDaoException;
import ru.otus.core.model.User;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.hibernate.sessionmanager.DatabaseSessionHibernate;
import ru.otus.hibernate.sessionmanager.SessionManagerHibernate;

import javax.persistence.EntityGraph;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class UserDaoHibernate implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoHibernate.class);
    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String LOGIN_FIELD = "login";

    private final SessionManagerHibernate sessionManager;

    public UserDaoHibernate(SessionManagerHibernate sessionManager) {
        this.sessionManager = sessionManager;
    }


    @Override
    public Optional<User> findById(long id) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        EntityGraph entityGraph = currentSession.getHibernateSession().getEntityGraph("user-entity-graph");
        Map<String, Object> properties = new HashMap<>();
        properties.put("javax.persistence.fetchgraph", entityGraph);

        try {
            return Optional.ofNullable(currentSession.getHibernateSession().find(User.class, id, properties));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findByLogin(String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        EntityGraph entityGraph = currentSession.getHibernateSession().getEntityGraph("user-entity-graph");

        Session session = currentSession.getHibernateSession();
        Query<User> query = session.createQuery("from User u where u.login=:login", User.class);
        query.setParameter("login", login);
        query.setHint("javax.persistence.loadgraph",entityGraph);
        User user = query.uniqueResult();

        return Optional.ofNullable(user);
    }

    @Override
    public long insertUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.persist(user);
            hibernateSession.flush();
            return user.getId();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void updateUser(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            hibernateSession.merge(user);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public void insertOrUpdate(User user) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        try {
            Session hibernateSession = currentSession.getHibernateSession();
            if (user.getId() > 0) {
                hibernateSession.merge(user);
                hibernateSession.flush();
            } else {
                hibernateSession.persist(user);
                hibernateSession.flush();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new UserDaoException(e);
        }
    }

    @Override
    public List<User> findByMask(String id, String name, String login) {
        DatabaseSessionHibernate currentSession = sessionManager.getCurrentSession();
        EntityGraph entityGraph = currentSession.getHibernateSession().getEntityGraph("user-entity-graph");

        Session session = currentSession.getHibernateSession();

        long longId = 0;
        if(id != null && (id.isEmpty())) { id = null; } // id.isBlank() ||
        if(id != null) { longId = Long.valueOf(id); }
        if(name != null && (name.isEmpty())) { name = null; } // name.isBlank() ||
        if(login != null && (login.isEmpty())) { login = null; } // login.isBlank() ||
        Query<User> query = session.createQuery(
                "from User u where " +
                        "(:id is null or u.id = :longId) " +
                        "and (:name is null or u.name like :name) " +
                        "and (:login is null or u.login like :login)", User.class);
        query.setParameter(ID_FIELD, id);
        query.setParameter("longId", longId);
        query.setParameter(NAME_FIELD, name);
        query.setParameter(LOGIN_FIELD, login);

        query.setHint("javax.persistence.loadgraph",entityGraph);
        return query.getResultList();
    }

    @Override
    public SessionManager getSessionManager() {
        return sessionManager;
    }
}
