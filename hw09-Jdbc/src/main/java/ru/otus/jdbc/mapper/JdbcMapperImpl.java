package ru.otus.jdbc.mapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.sessionmanager.SessionManager;
import ru.otus.jdbc.mapper.exceptions.JdbcMapperException;
import ru.otus.jdbc.sessionmanager.DatabaseSessionJdbc;
import ru.otus.jdbc.DbExecutor;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    public static final int FIRST_IDX_COLUMN = 1;
    private SessionManager sessionManager;
    private DbExecutor<T> dbExecutor;
    private EntityClassMetaData entityClassMetaData;
    private EntitySQLMetaData entitySQLMetaData;

    private static final Logger logger = LoggerFactory.getLogger(JdbcMapperImpl.class);

    public JdbcMapperImpl(SessionManager sessionManager, DbExecutor<T> dbExecutor, Class<T> typeParameterClass) {
        if (sessionManager == null) {
            throw new JdbcMapperException("SessionManager parameter is null.");
        }
        if (dbExecutor == null) {
            throw new JdbcMapperException("DbExecutor parameter is null.");
        }
        this.sessionManager = sessionManager;
        this.dbExecutor = dbExecutor;
        this.entityClassMetaData = new EntityClassMetaDataImpl(typeParameterClass);
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
    }

    private Connection getConnection() {
        if (sessionManager == null) {
            throw new JdbcMapperException("SessionManager is null.");
        }
        DatabaseSessionJdbc databaseSessionJdbc = (DatabaseSessionJdbc) sessionManager.getCurrentSession();
        return databaseSessionJdbc.getConnection();
    }

    @Override
    public long insert(T objectData) {
        if (objectData == null) {
            throw new JdbcMapperException("objectData is null.");
        }

        List<Object> params = getParams(objectData);
        String insertStatement = entitySQLMetaData.getInsertSql();
        try {
            Connection connection = getConnection();
            return dbExecutor.executeInsert(connection, insertStatement, params);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new JdbcMapperException(throwables.getMessage());
        }
    }

    private List<Object> getParams(T objectData) {
        List<Object> fieldValues = new ArrayList<>();
        List<Field> fieldList = entityClassMetaData.getAllFields();
        for (Field field : fieldList) {
            try {
                fieldValues.add(field.get(objectData));
            } catch (IllegalAccessException e) {
                throw new JdbcMapperException(e.getMessage());
            }
        }
        return fieldValues;
    }

    @Override
    public void update(T objectData) {

        if (objectData == null) {
            throw new JdbcMapperException("objectData is null.");
        }

        List<Object> params = new ArrayList<>();

        List<Field> fieldList = entityClassMetaData.getFieldsWithoutId();
        for (Field field : fieldList) {
            try {
                params.add(field.get(objectData));
            } catch (IllegalAccessException e) {
                logger.error(e.getMessage(), e);
                throw new JdbcMapperException(e.getMessage());
            }
        }

        Field idField = entityClassMetaData.getIdField();
        try {
            params.add(idField.get(objectData));
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new JdbcMapperException(e.getMessage());
        }

        try {
            dbExecutor.executeInsert(
                    getConnection(),
                    entitySQLMetaData.getUpdateSql(),
                    params);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new JdbcMapperException(e.getMessage());
        }
    }

    @Override
    public void insertOrUpdate(T objectData) {
        if (objectData == null) {
            throw new JdbcMapperException("objectData is null.");
        }

        // Если Id у объекта null, то вставка объекта в базу
        Field idField = entityClassMetaData.getIdField();
        try {
            Object idValue = idField.get(objectData);
            if (idValue == null) {
                insert(objectData);
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
            throw new JdbcMapperException(e.getMessage());
        }

        // Id есть, но кто его знает кто установил? Есть он в базе или нет?
        // Поэтому пытаемся изменить, если ошибка, то вставляем
        // Тут вопросы, ошибка из-за чего? Точно из-за того, что записи нет, или еще из-за чего
        // Надо бы exceptions проработать
        try {
            update(objectData);
        } catch (Throwable ex) {
            try {
                insert(objectData);
            } catch (Throwable e) {
                logger.error(e.getMessage(), e);
                throw new JdbcMapperException(e.getMessage());
            }
        }
    }

    @Override
    public T findById(Object id, Class<T> clazz) {
        T obj = null;
        try {
            obj = dbExecutor.executeSelect(
                    getConnection(),
                    entitySQLMetaData.getSelectByIdSql(),
                    id,
                    rs -> {
                        try {
                            // get sql statement field values
                            List<Object> params = new ArrayList<>();
                            if (rs.next()) {
                                ResultSetMetaData resultSetMetaData = rs.getMetaData();
                                int numberColumns = resultSetMetaData.getColumnCount(); // delete
                                for (int idx = FIRST_IDX_COLUMN; idx <= resultSetMetaData.getColumnCount(); idx++) {
                                    Object value = rs.getObject(idx);
                                    params.add(rs.getObject(idx));
                                }
                            }
                            return (T) entityClassMetaData.getConstructor().newInstance(params.toArray());
                        } catch (SQLException e) {
                            logger.error(e.getMessage(), e);
                            throw new JdbcMapperException(e.getMessage());
                        } catch (Exception e) {
                            logger.error(e.getMessage(), e);
                            throw new JdbcMapperException(e.getMessage());
                        }
                    }).get();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new JdbcMapperException(e.getMessage());
        }
        return obj;
    }
}
