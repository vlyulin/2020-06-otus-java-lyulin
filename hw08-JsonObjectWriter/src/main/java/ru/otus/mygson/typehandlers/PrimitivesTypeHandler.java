package ru.otus.mygson.typehandlers;

import com.google.gson.internal.Primitives;
import ru.otus.mygson.typehandlers.exceptions.IllegalAccessTypeHandlerException;
import ru.otus.mygson.typehandlers.exceptions.TypeHandlerException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

public class PrimitivesTypeHandler extends BaseTypeHandler {

    private final List<Class<?>> boxes = Arrays.asList(
            Byte.class,
            Short.class,
            Integer.class,
            Long.class,
            Float.class,
            Double.class,
            Boolean.class);

    protected String getValue(Object obj, Field field) {

        if (obj == null) return null;
        if (field == null) throw new IllegalArgumentException("field argument is null");

        try {
            Class<?> fieldClass = field.getType();
            if (fieldClass == byte.class) return Byte.toString(field.getByte(obj));
            if (fieldClass == short.class) return Short.toString(field.getShort(obj));
            if (fieldClass == int.class) return Integer.toString(field.getInt(obj));
            if (fieldClass == long.class) return Long.toString(field.getLong(obj));
            if (fieldClass == float.class) return Float.toString(field.getFloat(obj));
            if (fieldClass == double.class) return Double.toString(field.getDouble(obj));
            if (fieldClass == boolean.class) return Boolean.toString(field.getBoolean(obj));

            if (fieldClass == char.class)
                return "\"" + field.getChar(obj) + "\"";
            if (fieldClass.isAssignableFrom(String.class)) {
                String value = (String) field.get(obj);
                return (value == null) ? null : "\"" + field.get(obj) + "\"";
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalAccessTypeHandlerException("Illegal access to " + field.getName() + ": " + e.getMessage());
        }

        // Что-то удивительное прилетело
        throw new TypeHandlerException("Unhandled value: " + obj.toString());
    }

    @Override
    public String toJson(Object obj, Field field) {

        if (obj == null) return null;
        if (field == null) throw new IllegalArgumentException("field argument is null");

        Class<?> fieldClass = field.getType();
        if (!(fieldClass.isPrimitive() || Primitives.isWrapperType(fieldClass) || fieldClass.equals(String.class))) {
            // не примитивный тип, передача следующему обработчику
            return super.toJson(obj, field);
        }

        makeAccessible(field);

        String value = getValue(obj, field);
        if (value == null) return null;
        return "\"" + field.getName() + "\":" + value;
    }

    @Override
    public String toJson(Object obj) {

        if (obj == null) return null;

        Class<?> type = obj.getClass();
        // Не знаю как проще определить boxing для примитивных типов, кроме как contains
        if (!(type.isPrimitive() || boxes.contains(type) || type.equals(String.class))) {
            return super.toJson(obj);
        }

        if (type == Byte.class
                || type == Short.class
                || type == Integer.class
                || type == Long.class
                || type == Float.class
                || type == Double.class
                || type == Boolean.class
        ) return obj.toString();

        if (type == Character.class || type == String.class) return "\"" + obj.toString() + "\"";

        // Что-то удивительное прилетело
        throw new TypeHandlerException("Unhandled value: " + obj.toString());
    }
}
