package ru.otus.atm.exceptions;

public class ATMNotEnoughMoneyException extends RuntimeException {
    public ATMNotEnoughMoneyException(String message) {
        super(message);
    }
}
