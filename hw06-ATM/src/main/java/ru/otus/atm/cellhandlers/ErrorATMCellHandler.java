package ru.otus.atm.cellhandlers;

import ru.otus.banknotes.Banknote;
import ru.otus.atm.exceptions.ATMNotEnoughMoneyException;

import java.util.Map;

/*
 * Конечный обработчик, если запрашиваемая сумма не может быть выдана
 */
public class ErrorATMCellHandler extends BaseATMCellHandler {

    @Override
    public void withdrawCash(Map<Banknote, Integer> banknotes, int amount) {
        throw new ATMNotEnoughMoneyException("Запрашиваемая сумма "+amount+" не может быть выдана.");
    }
}
