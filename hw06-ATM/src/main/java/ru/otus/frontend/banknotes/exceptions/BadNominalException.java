package ru.otus.frontend.banknotes.exceptions;

public class BadNominalException extends RuntimeException {
    public BadNominalException(String message) {
        super(message);
    }
}
