package ru.otus.atm.cellhandlers;

import ru.otus.banknotes.Banknote;
import ru.otus.atm.commands.Command;

import java.util.Map;
import java.util.Stack;

/*
 * Ячейка банкомата хранящая купюры определенного номинала
 * Реализует шаблон Chain of Responsibility
 */
public class BaseATMCellHandler implements ATMCellHandler {

    private ATMCellHandler nextATMCellHandler;

    // TODO: не нравится, что надо реализовывыать все методы, хотя надо только setNext
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
