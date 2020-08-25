package ru.otus.atm.strategies;

import ru.otus.atm.cellhandlers.ATMCellHandler;
import ru.otus.banknotes.Banknote;

import java.util.List;
import java.util.Map;

public interface WithdrawStrategy {
    Map<Banknote, Integer> withdrawCash(List<ATMCellHandler> handlers, int amount);
}
