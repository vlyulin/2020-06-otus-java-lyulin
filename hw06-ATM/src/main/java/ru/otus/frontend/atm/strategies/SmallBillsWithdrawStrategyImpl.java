package ru.otus.frontend.atm.strategies;

import ru.otus.frontend.atm.cellhandlers.ATMCellHandler;
import ru.otus.frontend.atm.cellhandlers.ATMCellHandlerComparator;
import ru.otus.frontend.atm.cellhandlers.ErrorATMCellHandler;
import ru.otus.frontend.banknotes.Banknote;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Цель данной стратегии выдать запрашиваемую сумму мелкими купюрами (с разменом)
 * */
public class SmallBillsWithdrawStrategyImpl implements WithdrawStrategy {

    public static final int BIGGEST_SMALL_BILL = 1000;

    @Override
    public Map<Banknote, Integer> withdrawCash(List<ATMCellHandler> handlers, int amount) {

        List<ATMCellHandler> onlySmallBillsHandlers = handlers.stream()
                .filter(handler -> handler.getNominal() <= BIGGEST_SMALL_BILL)
                .collect(Collectors.toList());

        // Сортировка от меньшего к большему 50, 100, 200 и т.д.
        Collections.sort(onlySmallBillsHandlers, new ATMCellHandlerComparator());
        // Строим цепочку обработчиков с конца в начало, и начинаем обработчика ошибки
        // Если до нее добрались, то что-то где-то пошло не так и деньги лучше не отдавать
        // В результате первой ячейкой оказывается ячейка с наибольшим номиналом, а последняя ячейка выдает ошибку
        ATMCellHandler firstInChain = new ErrorATMCellHandler();
        for (ATMCellHandler handler: onlySmallBillsHandlers) {
            if(firstInChain != null) handler.setNext(firstInChain);
            firstInChain = handler;
        }
        Map<Banknote,Integer> banknotes = new HashMap<>(); // накопитель выдаваемых банкнот
        // Само снятие наличных
        firstInChain.withdrawCash(banknotes, amount);
        return banknotes;
    }
}
