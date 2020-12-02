package ru.otus.atm.cellhandlers;

import ru.otus.banknotes.Banknote;
import ru.otus.atm.commands.Command;
import ru.otus.atm.commands.UndoDepositCash;
import ru.otus.atm.commands.UndoWithdrawCommand;
import ru.otus.atm.cellhandlers.exceptions.CellHandlerException;

import java.util.Map;
import java.util.Stack;

public class CashATMCellHandler extends BaseATMCellHandler {

    private Banknote processedBanknote;
    private int existingNumberOfBanknotes = 0;
    private Stack<Command> commandHistory;

    // banknote - определяет тип банкноты, которая может быть обработана данной ячейкой
    public CashATMCellHandler(Banknote banknote, int numberOfBanknotes) {
        this.processedBanknote = banknote;
        this.existingNumberOfBanknotes = numberOfBanknotes;
    }

    @Override
    public Banknote getBanknote() {
        return processedBanknote.clone();
    }

    @Override
    public int getNominal() {
        return processedBanknote.getNominal();
    }

    @Override
    public void insertBanknotes(int amount) {
        if(amount <= 0) {
            throw new CellHandlerException(
                    "Ячейка для номинала: " + processedBanknote.getNominal() +
                    "\nПопытка добавления отрицательного количества банкнот"
            );
        }
        // сохраняем команду снятия банкнот в количестве numberOfBanknotes
        if(commandHistory != null) {
            commandHistory.push(new UndoDepositCash(this, amount));
        }
        this.existingNumberOfBanknotes += amount;
    }

    @Override
    public void undoInsertBanknotes(int amount) {
        if(amount <= 0) {
            throw new CellHandlerException(
                    "Ячейка для номинала: " + processedBanknote.getNominal() +
                            "\nПопытка восстановления отрицательного количества банкнот"
            );
        }
        this.existingNumberOfBanknotes -= amount;
    }

    @Override
    public int getBanknotesAmount() {
        return existingNumberOfBanknotes;
    }

    @Override
    public void withdrawCash(Map<Banknote, Integer> banknotes, int amount) {
        // определение сколько банкнот может быть выдано
        // при этом количество банкнот не может превышать количество банкнот находящихся
        // в ячейке на текущий момент
        int numberOfBanknotes = Math.min(amount / processedBanknote.getNominal(), existingNumberOfBanknotes);
        assert (numberOfBanknotes > 0);
        if (numberOfBanknotes > 0) {
            // добавляем количество купюр, которые могут быть выданы
            banknotes.put(this.processedBanknote, numberOfBanknotes);
            // сохраняем команду снятия банкнот в количестве numberOfBanknotes
            if(commandHistory != null) {
                commandHistory.push(new UndoWithdrawCommand(this, numberOfBanknotes));
            }
            // убавляем количество купюр в данной ячейке
            this.existingNumberOfBanknotes -= numberOfBanknotes;
        }

        // Остаточная сумма
        int residualAmount = amount - (numberOfBanknotes * processedBanknote.getNominal());
        if(residualAmount == 0) {
            // завершение обработки
            return;
        }
        // Вызов следующего обработчика для выдачи оставшейся суммы
        super.withdrawCash(banknotes, residualAmount);
    }

    @Override
    public void undoWithdrawCash(int amount) {
        if(amount <= 0) {
            throw new CellHandlerException(
                    "Ячейка для номинала: " + processedBanknote.getNominal()
                            + "\nПопытка отмены операции снятия с указанием отрицательного количества банкнот:  "
                            + amount
            );
        }
        this.existingNumberOfBanknotes += amount;
    }

    public void setCommandHistory(Stack<Command> commandHistory) {
        this.commandHistory = commandHistory;
    }
}
