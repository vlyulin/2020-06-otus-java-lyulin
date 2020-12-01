package ru.otus.frontend.mygson.typehandlers;

import ru.otus.frontend.mygson.typehandlers.exceptions.IllegalAccessTypeHandlerException;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Iterator;

public class CollectionsTypeHandler extends BaseTypeHandler {

    @Override
    public String toJson(Object obj, Field field) {

        if (obj == null) return null;
        if (field == null) throw new IllegalArgumentException("field argument is null");

        if (!Collection.class.isAssignableFrom(field.getType())) {
            return super.toJson(obj, field);
        }

        makeAccessible(field);

        StringBuilder result = new StringBuilder();

        Iterator<?> items;
        try {
            Collection<?> collection = (Collection<?>) field.get(obj);
            // Неинициализированная коллекция? Просто проходим мимо
            if (collection == null) return null;
            items = collection.iterator();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new IllegalAccessTypeHandlerException("Illegal access to " + field.getName() + ": " + e.getMessage());
        }

        // Обработка элементов массива
        TypeHandler typeHandlersChain = TypeHandlersChain.getChain();
        result.append('[');
        String prefix = "";
        while (items.hasNext()) {
            Object item = items.next();
            String jsonValue = typeHandlersChain.toJson(item);
            result.append(prefix);
            result.append(jsonValue);
            prefix = ",";
        }
        result.append(']');
        return "\"" + field.getName() + "\":" + result.toString();
    }
}
