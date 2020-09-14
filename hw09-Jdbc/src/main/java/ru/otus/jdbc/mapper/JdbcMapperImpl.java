package ru.otus.jdbc.mapper;

import ru.otus.jdbc.DbExecutor;
import ru.otus.jdbc.mapper.exceptions.JdbcMapperException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JdbcMapperImpl<T> implements JdbcMapper<T> {

    private Connection connection;
    private DbExecutor dbExecutor;
    private EntitySQLMetaData entitySQLMetaData;

    public JdbcMapperImpl(Connection connection, DbExecutor dbExecutor, Class<T> typeParameterClass) {
        if(connection == null) {
            throw new JdbcMapperException("Connection is null.");
        }
        if(dbExecutor == null) {
            throw new JdbcMapperException("dbExecutor is null.");
        }
        this.connection = connection;
        this.dbExecutor = dbExecutor;
        EntityClassMetaData entityClassMetaData = new EntityClassMetaDataImpl(typeParameterClass);
        this.entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
    }

    @Override
    public void insert(T objectData) {
        List<Object> params = new ArrayList();
        String insertStatement = entitySQLMetaData.getInsertSql();
        try {
            dbExecutor.executeInsert(connection, insertStatement, params);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new JdbcMapperException(throwables.getMessage());
        }
    }

    @Override
    public void update(T objectData) {

    }

    @Override
    public void insertOrUpdate(T objectData) {

    }

    @Override
    public T findById(Object id, Class<T> clazz) {
        return null;
    }
}
