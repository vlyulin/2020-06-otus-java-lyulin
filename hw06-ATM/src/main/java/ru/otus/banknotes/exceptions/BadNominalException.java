package ru.otus.banknotes.exceptions;

public class BadNominalException extends RuntimeException {
    public BadNominalException(String message) {
        super(message);
    }
}
