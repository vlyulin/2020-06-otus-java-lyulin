package ru.otus.jdbc.mapper;

import ru.otus.anotations.Id;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EntityClassMetaDataImpl<T> implements EntityClassMetaData<T> {

    private Class<T> genericTypeClass;
    private List<Constructor<?>> constructors;
    private List<Constructor<?>> constructors; // TODO: почему именно <?>
    private List<Field> idFields = new ArrayList<>();
    private List<Field> fields = new ArrayList<>();

    protected static void makeAccessible(Field field) {
        if (field == null) return;

        if (!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }

    public EntityClassMetaDataImpl(Class<T> typeParameterClass) {
        genericTypeClass = typeParameterClass;
        constructors = Arrays.asList(typeParameterClass.getConstructors());
        for(final Field field: typeParameterClass.getDeclaredFields()) {
            makeAccessible(field);
            if(field.isAnnotationPresent(Id.class)) {
                idFields.add(field);
            } else {
                fields.add(field);
            }
        }
    }

    @Override
    public String getName() {
        return genericTypeClass.getSimpleName();
    }

    @Override
    public Constructor<T> getConstructor() {
        return (Constructor<T>) constructors.get(0);
    }

    @Override
    public Field getIdField() {
        return idFields.get(0);
    }

    @Override
    public List<Field> getAllFields() {
        List<Field> fieldsList = new ArrayList<>();
        fieldsList.addAll(idFields);
        fieldsList.addAll(fields);
        return fieldsList;
    }

    @Override
    public List<Field> getFieldsWithoutId() {
        return new ArrayList<Field>(fields);
    }
}
