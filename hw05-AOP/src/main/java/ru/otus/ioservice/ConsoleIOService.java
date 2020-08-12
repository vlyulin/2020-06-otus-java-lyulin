package ru.otus.ioservice;

public class ConsoleIOService implements IOService {

    public ConsoleIOService() {
    }

    @Override
    public void printLn(String msg) {
        System.out.println(msg);
    }

    @Override
    public void print(String msg) {
        System.out.print(msg);
    }
}
