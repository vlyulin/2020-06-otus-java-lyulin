package ru.otus.ioservice;

import java.util.Scanner;

public class ConsoleIOService implements IOService {

    private Scanner in;

    public ConsoleIOService() {
        in = new Scanner(System.in);
    }

    @Override
    public void printLn(String msg) {
        System.out.println(msg);
    }

    @Override
    public void print(String msg) {
        System.out.print(msg);
    }

    @Override
    public int readInt() {
        if (in.hasNextInt()) {
            return in.nextInt();
        }
        else {
            in.next();
            throw new IllegalArgumentException();
        }
    }
}
