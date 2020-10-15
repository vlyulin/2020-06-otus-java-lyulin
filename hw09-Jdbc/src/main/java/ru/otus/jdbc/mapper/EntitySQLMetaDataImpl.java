package ru.otus.jdbc.mapper;

import java.lang.reflect.Field;
import java.util.List;

public class EntitySQLMetaDataImpl implements EntitySQLMetaData {

    private EntityClassMetaData entityClassMetaData;

    public EntitySQLMetaDataImpl(EntityClassMetaData entityClassMetaData) {
        this.entityClassMetaData = entityClassMetaData;
    }

    // Make all fields sequence like "field1, field2, ..."
    private void getFiledSequence(StringBuilder stringBuilder) {
        List<Field> fieldsList = entityClassMetaData.getAllFields();
        String prefix = "";
        for(Field field: fieldsList) {
            stringBuilder.append(prefix);
            stringBuilder.append(field.getName().toUpperCase());
            prefix = ", ";
        }
    }

    // Make "SELECT ... FROM T
    private void getSelectFrom(StringBuilder stringBuilder) {
        stringBuilder.append("SELECT ");
        getFiledSequence(stringBuilder);
        stringBuilder.append(" FROM ");
        stringBuilder.append(entityClassMetaData.getName().toUpperCase());
    }

    @Override
    // Make SELECT * FROM CLASS;
    public String getSelectAllSql() {
        StringBuilder stringBuilder = new StringBuilder();
        getSelectFrom(stringBuilder);
        stringBuilder.append(";");
        return stringBuilder.toString();
    }

    @Override
    // "SELECT * FROM USER WHERE IdField = ?;"
    public String getSelectByIdSql() {
        StringBuilder stringBuilder = new StringBuilder();
        getSelectFrom(stringBuilder);
        stringBuilder.append(" WHERE ");
        stringBuilder.append(entityClassMetaData.getIdField().getName().toUpperCase());
        stringBuilder.append(" = ?;");
        return stringBuilder.toString();
    }

    @Override
    // INSERT INTO T (filed list) VALUES (?, ?, ...);
    public String getInsertSql() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("INSERT INTO ");
        stringBuilder.append(entityClassMetaData.getName().toUpperCase());
        stringBuilder.append(" (");
        getFiledSequence(stringBuilder);
        stringBuilder.append(") VALUES (");
        int fieldsNumber = entityClassMetaData.getAllFields().size();
        String prefix = "";
        for(int idx = 0; idx < fieldsNumber; idx++) {
            stringBuilder.append(prefix);
            stringBuilder.append('?');
            prefix = ", ";
        }
        stringBuilder.append(");");
        return stringBuilder.toString();
    }

    @Override
    // UPDATE T SET field1 = ?, field2 = ?, ... WHERE IdField = ?;
    public String getUpdateSql() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("UPDATE ");
        stringBuilder.append(entityClassMetaData.getName().toUpperCase());
        stringBuilder.append(" SET ");
        List<Field> fieldsList = entityClassMetaData.getFieldsWithoutId();
        String prefix = "";
        for(Field field: fieldsList) {
            stringBuilder.append(prefix);
            stringBuilder.append(field.getName().toUpperCase());
            stringBuilder.append(" = ?");
            prefix = ", ";
        }
        stringBuilder.append(" WHERE ");
        stringBuilder.append(entityClassMetaData.getIdField().getName().toUpperCase());
        stringBuilder.append(" = ?;");
        return stringBuilder.toString();
    }
}
