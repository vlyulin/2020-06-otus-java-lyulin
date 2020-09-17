package ru.otus.jdbc.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.core.model.User;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EntitySQLMetaDataImplTest {

    public static final String SELECT_ALL_FROM_USER = "SELECT ID, NAME, AGE FROM USER;";
    public static final String SELECT_USER_BY_ID = "SELECT ID, NAME, AGE FROM USER WHERE ID = ?;";
    public static final String INSERT_STATEMENT = "INSERT INTO USER (ID, NAME, AGE) VALUES (?, ?, ?);";
    public static final String UPDATE_STATEMENT = "UPDATE USER SET NAME = ?, AGE = ? WHERE ID = ?;";
    public static final String ID_FIELD_NAME = "id";

    private Class<User> userClazz = User.class;

    private EntityClassMetaData getMockEntityClassMetaData() {

        EntityClassMetaData entityClassMetaData = mock(EntityClassMetaData.class);
        // Get name
        given(entityClassMetaData.getName()).willReturn(userClazz.getSimpleName());
        // All fields
        List<Field> allFiledList = Arrays.asList(userClazz.getDeclaredFields());
        given(entityClassMetaData.getAllFields()).willReturn(allFiledList);
        // Id field
        Field idField = null;
        for(Field field: allFiledList) {
            String fieldName = field.getName();
            if(field.getName().equals(ID_FIELD_NAME)) {
                idField = field;
                break;
            }
        }
        given(entityClassMetaData.getIdField()).willReturn(idField);
        // All fields without id
        List<Field> allFiledListWithoutId = new ArrayList<>(allFiledList);
        allFiledListWithoutId.remove(idField);
        given(entityClassMetaData.getFieldsWithoutId()).willReturn(allFiledListWithoutId);
        //
        return entityClassMetaData;
    }

    @Test
    @DisplayName("Test Select * from class")
    void getSelectAllSql() {
        EntityClassMetaData entityClassMetaData = getMockEntityClassMetaData();
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
        assertThat(entitySQLMetaData.getSelectAllSql()).isEqualTo(SELECT_ALL_FROM_USER);
    }

    @Test
    void getSelectByIdSql() {
        EntityClassMetaData entityClassMetaData = getMockEntityClassMetaData();
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
        assertThat(entitySQLMetaData.getSelectByIdSql()).isEqualTo(SELECT_USER_BY_ID);
    }

    @Test
    void getInsertSql() {
        EntityClassMetaData entityClassMetaData = getMockEntityClassMetaData();
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
        assertThat(entitySQLMetaData.getInsertSql()).isEqualTo(INSERT_STATEMENT);
    }

    @Test
    void getUpdateSql() {
        EntityClassMetaData entityClassMetaData = getMockEntityClassMetaData();
        EntitySQLMetaData entitySQLMetaData = new EntitySQLMetaDataImpl(entityClassMetaData);
        assertThat(entitySQLMetaData.getUpdateSql()).isEqualTo(UPDATE_STATEMENT);
    }
}