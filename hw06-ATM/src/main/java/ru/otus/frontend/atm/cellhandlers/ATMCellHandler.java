package ru.otus.frontend.atm.cellhandlers;

import ru.otus.frontend.banknotes.Banknote;
import ru.otus.frontend.atm.commands.Command;

import java.util.Map;
import java.util.Stack;

/* Банковская кассета для хранения банкнот определенного номинала */
/* Используется шаблон Chain of Responsibility */
public interface ATMCellHandler {
    Banknote getBanknote();
    int getNominal();
    void insertBanknotes(int amount);
    // отдельный метод для отката операции добавления денег со своей защитой (в будущем)
    void undoInsertBanknotes(int amount);
    int getBanknotesAmount();
    // banknotes - это накопитель для выдаваемых банкнот
    void withdrawCash(Map<Banknote, Integer> banknotes, int amount);
    // отдельный метод для отката операции снятия денег со своей защитой (в будущем)
    void undoWithdrawCash(int amount);
    void setNext(ATMCellHandler handler);
    void setCommandHistory(Stack<Command> commandHistory);
}
