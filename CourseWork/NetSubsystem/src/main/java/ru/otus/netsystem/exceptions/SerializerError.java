package ru.otus.netsystem.exceptions;

public class SerializerError extends RuntimeException {

    public SerializerError(String message, Throwable cause) {
        super(message, cause);
    }
}
