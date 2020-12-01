package ru.otus.frontend.mygson.typehandlers;

import com.google.gson.internal.Primitives;
import ru.otus.frontend.mygson.typehandlers.exceptions.IllegalAccessTypeHandlerException;

import java.lang.reflect.Field;
import java.util.Collection;

public class ClassTypeHandler extends BaseTypeHandler {

    @Override
    public String toJson(Object obj, Field field) {

        if (obj == null) return null;
        if (field == null) throw new IllegalArgumentException("field argument is null");

        Class<?> fieldClass = field.getType();
        if (fieldClass.isPrimitive()
                || Primitives.isWrapperType(fieldClass)
                || fieldClass.equals(String.class)
                || field.getType().isArray()
                || Collection.class.isAssignableFrom(field.getType())) {
            // не класс
            return super.toJson(obj, field);
        }

        makeAccessible(field);

        try {
            Object fieldValue = field.get(obj);
            // Если это не примитивный тип и экземпляр не определен, то его не обрабатываем
            if (fieldValue == null) return null;
            // Иначе форматируем
            return "\"" + field.getName() + "\":" + toJson(fieldValue);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalAccessTypeHandlerException("Illegal Access to " + field.getName() + " field.");
        }
    }

    @Override
    public String toJson(Object obj) {
        if (obj == null) return null;

        StringBuilder result = new StringBuilder();
        TypeHandler typeHandlersChain = TypeHandlersChain.getChain();

        result.append('{');
        Class<?> clazz = obj.getClass();
        String prefix = "";
        do {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String value = typeHandlersChain.toJson(obj, field);
                if (value != null) {
                    result.append(prefix);
                    result.append(value);
                    prefix = ",";
                }
            }
            // Путь наверх, по полям родителя
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        result.append('}');

        return result.toString();
    }
}
