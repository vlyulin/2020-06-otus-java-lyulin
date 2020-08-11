package ru.otus;

public class Main {
    public static void main(String[] args) {
        SomeInterface myClass = new TestLogging();
        myClass.calculation(1);
        myClass.calculation(1,2);
        myClass.calculation(1,2,"3");
    }
}
