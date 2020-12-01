package ru.otus.frontend.mygson.typehandlers;

public class TypeHandlersChain {
    public static TypeHandler getChain() {

        // Формирование chain of responsibility из обработчиков типов
        TypeHandler primitivesTypeHandler = new PrimitivesTypeHandler();
        TypeHandler collectionsTypeHandler = new CollectionsTypeHandler();
        ArraysTypeHandler arraysTypeHandler = new ArraysTypeHandler();
        ClassTypeHandler classTypeHandler = new ClassTypeHandler();

        primitivesTypeHandler.setNext(collectionsTypeHandler);
        collectionsTypeHandler.setNext(arraysTypeHandler);
        arraysTypeHandler.setNext(classTypeHandler);

        return primitivesTypeHandler;
    }
}
