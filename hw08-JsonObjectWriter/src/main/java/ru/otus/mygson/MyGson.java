package ru.otus.mygson;

import ru.otus.mygson.typehandlers.*;

public class MyGson {

    public String toJson(Object obj) {
        TypeHandler typeHandlersChain = TypeHandlersChain.getChain();
        return typeHandlersChain.toJson(obj);
    }
}
