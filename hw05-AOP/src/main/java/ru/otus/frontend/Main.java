package ru.otus.frontend;

import ru.otus.frontend.ioc.Ioc;

public class Main {
    public static void main(String[] args) {
        SomeInterface someInterfaceObj = Ioc.createSomeInterfaceObj();
        someInterfaceObj.calculation(1);
        someInterfaceObj.calculation(1,2);
        someInterfaceObj.calculation(1,2,"str 3");
    }
}
