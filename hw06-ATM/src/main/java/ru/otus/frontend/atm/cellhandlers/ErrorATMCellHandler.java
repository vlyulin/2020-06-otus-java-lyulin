package ru.otus.frontend.atm.cellhandlers;

import ru.otus.frontend.banknotes.Banknote;
import ru.otus.frontend.atm.exceptions.ATMNotEnoughMoneyException;

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
