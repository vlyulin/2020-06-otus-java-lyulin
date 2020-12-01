package ru.otus.frontend.jdbc.mapper;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.frontend.core.model.User;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EntityClassMetaDataImplTest {

    public static final String USER_CLASS_NAME = "User";
    public static final String USER_CLASS_CONSTRUCTOR_NAME = "ru.otus.core.model.User";
    public static final String USER_ID_FIELD = "id";
    private final List<String> referenceFieldsList = Arrays.asList("id", "name", "age");

    @Test
    @DisplayName("Test getName")
    public void testGetName() {
        EntityClassMetaDataImpl<User> entityClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        assertThat(entityClassMetaData.getName()).isEqualTo(USER_CLASS_NAME);
    }

    @Test
    @DisplayName("Test getConstructor")
    public void testGetConstructor() {
        EntityClassMetaDataImpl<User> entityClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        assertThat(entityClassMetaData.getConstructor().getName()).isEqualTo(USER_CLASS_CONSTRUCTOR_NAME);
    }

    @Test
    @DisplayName("Test getIdField")
    public void testGetIdField() {
        EntityClassMetaDataImpl<User> entityClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        assertThat(entityClassMetaData.getIdField().getName()).isEqualTo(USER_ID_FIELD);
    }

    @Test
    @DisplayName("Test getAllFields")
    public void testAllFields() {
        EntityClassMetaDataImpl<User> entityClassMetaData = new EntityClassMetaDataImpl<>(User.class);
        List<Field> fieldsList = entityClassMetaData.getAllFields();
        for(Field field: fieldsList) {
            assertThat(referenceFieldsList).contains(field.getName());
        }
    }
}