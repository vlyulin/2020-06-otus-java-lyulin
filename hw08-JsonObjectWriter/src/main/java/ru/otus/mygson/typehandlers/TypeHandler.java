package ru.otus.mygson.typehandlers;

import java.lang.reflect.Field;

public interface TypeHandler {
    void setNext(TypeHandler typeHandler);
    String toJson(Object obj, Field field);
    String toJson(Object obj);
}
