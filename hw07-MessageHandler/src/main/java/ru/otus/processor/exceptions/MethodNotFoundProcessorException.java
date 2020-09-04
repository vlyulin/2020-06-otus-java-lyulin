package ru.otus.processor.exceptions;

public class MethodNotFoundProcessorException extends RuntimeException {
    public MethodNotFoundProcessorException(String message) {
        super(message);
    }
}
