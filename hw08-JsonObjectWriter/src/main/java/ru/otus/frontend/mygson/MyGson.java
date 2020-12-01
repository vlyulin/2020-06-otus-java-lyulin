package ru.otus.frontend.mygson;

import ru.otus.frontend.mygson.typehandlers.TypeHandler;
import ru.otus.frontend.mygson.typehandlers.TypeHandlersChain;

public class MyGson {

    public String toJson(Object obj) {
        TypeHandler typeHandlersChain = TypeHandlersChain.getChain();
        return typeHandlersChain.toJson(obj);
    }
}
