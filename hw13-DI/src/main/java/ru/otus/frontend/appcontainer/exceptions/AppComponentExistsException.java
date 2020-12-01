package ru.otus.frontend.appcontainer.exceptions;

public class AppComponentExistsException extends RuntimeException {
    public AppComponentExistsException(String message) {
        super(message);
    }
}
