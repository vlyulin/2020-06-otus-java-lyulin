package ru.otus.frontend.atm.commands;

import ru.otus.frontend.atm.cellhandlers.ATMCellHandler;

public class UndoDepositCash implements Command {
    private ATMCellHandler atmCellHandler;
    private int amount;

    public UndoDepositCash(ATMCellHandler atmCellHandler, int amount) {
        this.atmCellHandler = atmCellHandler;
        this.amount = amount;
    }

    @Override
    public void undo() {
        atmCellHandler.undoInsertBanknotes(amount);
    }
}
