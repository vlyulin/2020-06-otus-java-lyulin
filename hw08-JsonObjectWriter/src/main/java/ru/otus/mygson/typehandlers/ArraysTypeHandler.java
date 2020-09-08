package ru.otus.mygson.typehandlers;

import ru.otus.mygson.typehandlers.exceptions.IllegalAccessTypeHandlerException;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

public class ArraysTypeHandler extends BaseTypeHandler {
    @Override
    public String toJson(Object obj, Field field) {

        if (obj == null) return null;
        if (field == null) throw new IllegalArgumentException("field argument is null");

        if (!field.getType().isArray()) {
            return super.toJson(obj, field);
        }

        makeAccessible(field);

        StringBuilder result = new StringBuilder();
        Object[] array;
        try {
            array = (Object[]) field.get(obj);
            // Неинициализированный массив, просто не обрабатываем
            if( array == null ) return null;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalAccessTypeHandlerException("Illegal access to " + field.getName() + ": " + e.getMessage());
        }
        // Обработка элементов массива
        TypeHandler typeHandlersChain = TypeHandlersChain.getChain();
        String prefix = "";
        result.append('[');
        for(int idx=0; idx < Array.getLength(array); idx++) {
            Object value = Array.get(array,idx);
            String jsonValue = typeHandlersChain.toJson(value);
            result.append(prefix);
            result.append(jsonValue);
            prefix = ",";
        }
        result.append(']');
        // отформатированный вывод массива
        return "\"" + field.getName() + "\":" + result.toString();
    }
}
