package ru.otus.frontend.atm.commands;

import ru.otus.frontend.atm.cellhandlers.ATMCellHandler;

// Сохранение операции для возможности восстановления состояния банкомата в случае ошибки при снятии
public class UndoWithdrawCommand implements Command {
    private ATMCellHandler atmCellHandler;
    private int amount;

    public UndoWithdrawCommand(ATMCellHandler atmCellHandler, int amount) {
        this.atmCellHandler = atmCellHandler;
        this.amount = amount;
    }

    @Override
    public void undo() {
        atmCellHandler.undoWithdrawCash(amount);
    }
}
