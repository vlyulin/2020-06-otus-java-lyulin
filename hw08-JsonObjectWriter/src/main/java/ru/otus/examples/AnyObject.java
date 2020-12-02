package ru.otus.examples;

import java.util.*;

public class AnyObject extends SuperAnyClass {
    // Arrays
    Short[] nullShortArray; // проверка на неинициализированный массив
    Item[] emptyItems = {}; // проверка на пустой массив
    Item[] itemsArray = {new Item()}; //массив классов
    // массивы примитивщины
    Short[] shortTypeArray = {1, 2, 3, 4, 5};
    Double[] doubleTypeArray = {1.0, 2.0, 3.0, 4.0, 5.0};
    String[] stringTypeArray = {"first", "second", "third"};

    // Collections
    List<Byte> nullByteList; // проверка на неинициализированный массив
    List<Byte> emptyByteList = new ArrayList<>(); // проверка на пустой список
    List<Integer> integerTypeList = Arrays.asList(1,2,3,4,5);
    Set<Integer> shortTypeSet = new HashSet<>(){{
        add(300);
        add(400);
        add(500);
    }};
}
