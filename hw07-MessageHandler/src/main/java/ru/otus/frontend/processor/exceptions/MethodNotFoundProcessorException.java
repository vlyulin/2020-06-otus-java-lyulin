package ru.otus.frontend.processor.exceptions;

public class MethodNotFoundProcessorException extends RuntimeException {
    public MethodNotFoundProcessorException(String message) {
        super(message);
    }
}
