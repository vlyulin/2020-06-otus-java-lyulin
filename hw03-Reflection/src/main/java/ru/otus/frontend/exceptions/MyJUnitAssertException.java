package ru.otus.frontend.exceptions;

public class MyJUnitAssertException extends RuntimeException {
    public MyJUnitAssertException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
