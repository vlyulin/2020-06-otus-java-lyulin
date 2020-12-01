package ru.otus.frontend.atm;

import ru.otus.frontend.banknotes.Banknote;
import ru.otus.frontend.atm.results.OperationResult;

import java.util.Map;

public interface ATM {
    // - принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
    OperationResult DepositCash(Map<Banknote, Integer> banknotes);
    // - выдавать запрошенную сумму минимальным количеством банкнот или ошибку если сумму нельзя выдать
    Map<Banknote, Integer> withdrawCash(int amount);
    // - выдавать сумму остатка денежных средств
    void printATMState();
}
