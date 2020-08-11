package ru.otus;

import ru.otus.annotations.Log;

public class TestLogging implements SomeInterface {
    @Log
    @Override
    public void calculation(int param) {
        System.out.println("Calculation: int param: " + param);
    }

    @Log
    @Override
    public void calculation(int param1, int param2) {
        System.out.println("Calculation: int param1: " + param1 + ", int param2: " + param2);
    }

    @Log
    @Override
    public void calculation(int param1, int param2, String param3) {
        System.out.println("Calculation: int param1: " + param1 + ", int param2: "
                + param2 + ", String param3: " + param3);
    }
}
