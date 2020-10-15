package ru.otus.mygson.typehandlers;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class BaseTypeHandler implements TypeHandler {
    private TypeHandler nextTypeHandler;

    @Override
    public void setNext(TypeHandler typeHandler) {
        nextTypeHandler = typeHandler;
    }

    @Override
    public String toJson(Object obj, Field field) {
        if (nextTypeHandler != null) return nextTypeHandler.toJson(obj, field);
        return null;
    }

    @Override
    public String toJson(Object obj) {
        if (nextTypeHandler != null) return nextTypeHandler.toJson(obj);
        return null;
    }

    protected static void makeAccessible(Field field) {
        if (field == null) return;

        if (!Modifier.isPublic(field.getModifiers()) ||
                !Modifier.isPublic(field.getDeclaringClass().getModifiers())) {
            field.setAccessible(true);
        }
    }
}
