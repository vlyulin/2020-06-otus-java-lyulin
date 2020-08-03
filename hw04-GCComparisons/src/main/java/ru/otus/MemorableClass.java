package ru.otus;

import java.util.ArrayList;
import java.util.List;

// Пожиратель памяти
public class MemorableClass {

    private int size;
    // Утечка пямяти организована по статье
    // https://topjava.ru/blog/java-memory-leaks
    // раздел 3.1 Утечки памяти из-за статических полей

    // Вариант без утечки
    public List<Double> list = new ArrayList<>();
    // Вариант с утечкой
//    public static List<Double> list = new ArrayList<>();

    public MemorableClass(int size) {
        this.size = size;
    }

    public void populate() {
        for (int i = 0; i < size; i++) {
            list.add(Math.random());
        }
    }
}
