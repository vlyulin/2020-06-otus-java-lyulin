package ru.otus.banknotes.exceptions;

public class BanknoteException extends RuntimeException {
    public BanknoteException(String message) {
        super(message);
    }
}
