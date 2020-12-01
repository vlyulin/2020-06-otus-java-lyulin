package ru.otus.frontend.atm.strategies;

import ru.otus.frontend.atm.cellhandlers.ATMCellHandler;
import ru.otus.frontend.banknotes.Banknote;

import java.util.List;
import java.util.Map;

public interface WithdrawStrategy {
    Map<Banknote, Integer> withdrawCash(List<ATMCellHandler> handlers, int amount);
}
