package ru.otus.frontend.atm.exceptions;

public class ATMNotEnoughMoneyException extends RuntimeException {
    public ATMNotEnoughMoneyException(String message) {
        super(message);
    }
}
