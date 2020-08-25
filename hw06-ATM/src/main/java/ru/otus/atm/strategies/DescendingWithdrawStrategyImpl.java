package ru.otus.atm.strategies;

import ru.otus.atm.cellhandlers.ATMCellHandler;
import ru.otus.atm.cellhandlers.ATMCellHandlerComparator;
import ru.otus.atm.cellhandlers.ErrorATMCellHandler;
import ru.otus.banknotes.Banknote;

import java.util.*;

/*
* Цель данной стратегии выдать запрашиваемую сумму купюрами с наибольшим номиналом
* Реализует пункт задания:
* - выдавать запрошенную сумму минимальным количеством банкнот или ошибку если сумму нельзя выдать
* */
public class DescendingWithdrawStrategyImpl implements WithdrawStrategy {

    @Override
    public Map<Banknote, Integer> withdrawCash(List<ATMCellHandler> handlers, int amount) {

        // Сортировка от меньшего к большему 50, 100, 200 и т.д.
        Collections.sort(handlers, new ATMCellHandlerComparator());
        // Строим цепочку обработчиков с конца в начало, и начинаем обработчика ошибки
        // Если до нее добрались, то что-то где-то пошло не так и деньги лучше не отдавать
        // В результате первой ячейкой оказывается ячейка с наибольшим номиналом, а последняя ячейка выдает ошибку
        ATMCellHandler firstInChain = new ErrorATMCellHandler();
        for (ATMCellHandler handler: handlers) {
            if(firstInChain != null) handler.setNext(firstInChain);
            firstInChain = handler;
        }
        Map<Banknote,Integer> banknotes = new HashMap<>(); // накопитель выдаваемых банкнот
        // Само снятие наличных
        firstInChain.withdrawCash(banknotes, amount);
        return banknotes;
    }
}
