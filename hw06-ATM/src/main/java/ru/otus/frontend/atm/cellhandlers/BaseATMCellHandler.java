package ru.otus.frontend.atm.cellhandlers;

import ru.otus.frontend.banknotes.Banknote;
import ru.otus.frontend.atm.commands.Command;

import java.util.Map;
import java.util.Stack;

/*
 * Ячейка банкомата хранящая купюры определенного номинала
 * Реализует шаблон Chain of Responsibility
 */
public abstract class BaseATMCellHandler implements ATMCellHandler {

    private ATMCellHandler nextATMCellHandler;

    @Override
    public Banknote getBanknote() {
        return null;
    }

    @Override
    public int getNominal() {
        return 0;
    }

    @Override
    public void insertBanknotes(int amount) {
        return;
    }

    @Override
    public void undoInsertBanknotes(int amount) {
        return;
    }

    @Override
    public int getBanknotesAmount() {
        return 0;
    }

    @Override
    public void withdrawCash(Map<Banknote, Integer> banknotes, int amount) {
        nextATMCellHandler.withdrawCash(banknotes, amount);
    }

    @Override
    public void undoWithdrawCash(int amount) {
        return;
    }

    @Override
    public void setNext(ATMCellHandler handler) {
        this.nextATMCellHandler = handler;
    }

    @Override
    public void setCommandHistory(Stack<Command> commandHistory) {
        return;
    }
}
