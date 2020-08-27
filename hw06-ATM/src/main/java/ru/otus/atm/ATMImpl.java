package ru.otus.atm;

import ru.otus.atm.cellhandlers.ATMCellHandler;
import ru.otus.atm.cellhandlers.ATMCellHandlerComparator;
import ru.otus.atm.commands.Command;
import ru.otus.atm.results.OperationResult;
import ru.otus.atm.results.OperationResultImpl;
import ru.otus.atm.strategies.WithdrawStrategy;
import ru.otus.banknotes.Banknote;
import ru.otus.banknotes.BanknoteImpl;
import ru.otus.banknotes.NOMINAL;
import ru.otus.atm.exceptions.ATMException;
import ru.otus.atm.exceptions.ATMNotEnoughMoneyException;
import ru.otus.banknotes.exceptions.BadNominalException;
import ru.otus.ioservice.IOService;

import java.util.*;

public class ATMImpl implements ATM {
    // Обрабатываемые номиналы
    private List<NOMINAL> nominals;
    // Сервис ввода/вывода
    private IOService ioService;
    // Кассеты с банкнотами
    private List<ATMCellHandler> atmCellHandlers = new ArrayList<>();;
    // Стратегия выдачи денег (крупные, с разменом)
    private WithdrawStrategy currentWithdrawStrategy;
    // Сохранение числа выданных банкнот в каждой ячейке при снятии для вохможности восстановления в случае ошибки
    private Stack<Command> commandHistory = new Stack<>();

    public ATMImpl setNominals(NOMINAL[] nominals) {
        this.nominals = Arrays.asList(nominals);
        return this;
    }

    public ATMImpl setIOService(IOService ioService) {
        this.ioService = ioService;
        return this;
    }

    public ATMImpl setCellHandlers(List<ATMCellHandler> handlers) {
        this.atmCellHandlers = handlers;
        // Добавим в каждый стэк для возможности восстановления
        for (ATMCellHandler handler: handlers) {
            handler.setCommandHistory(this.commandHistory);
        }
        return this;
    }

    public ATMImpl setWithdrawStrategy(WithdrawStrategy withdrawStrategy) {
        this.currentWithdrawStrategy = withdrawStrategy;
        return this;
    }

    @Override
    public Map<Banknote, Integer> withdrawCash(int amount) {
        int askedAmount = amount; // сохраняем запрашиваемую сумму для вывода в ошибке
        if (currentWithdrawStrategy == null) {
            throw new ATMNotEnoughMoneyException("Не указана стратегия снятия денег.");
        }
        try {
            // Очищаем предыдущую историю команд
            commandHistory.clear();
            // Само снятие
            return this.currentWithdrawStrategy.withdrawCash(this.atmCellHandlers, amount);
        } catch (ATMNotEnoughMoneyException exception) {
            // Восстанавливаем состояние банкомата до момента этой операции снятия
            undo();
            // Подправляем сумму в ошибке заменяя недостающую сумму на запрашиваемую сумму
            throw new ATMNotEnoughMoneyException("Запрашиваемая сумма " + askedAmount + " не может быть выдана.");
        }
    }

    private void undo() {
        if(commandHistory == null || commandHistory.empty()) return;
        while(!commandHistory.empty()) {
            Command command = commandHistory.pop();
            command.undo();
        }
    }

    // Как мне кажется, внесенные деньги складываются в банкомате отдельно и не выдаются обратно
    // Но так как задание гласит
    // - принимать банкноты разных номиналов (на каждый номинал должна быть своя ячейка)
    // То будем распихивать в те же ячейки
    private void putCashIntoCell(Banknote banknote, int amount) {
        atmCellHandlers.stream()
                .filter(cell -> cell.getBanknote().equals(banknote))
                .findFirst().ifPresentOrElse(
                cell -> cell.insertBanknotes(amount),
                () -> {
                    throw new ATMException("Не найдена ячейка для номинала " + banknote.getNominal());
                });
    }

    @Override
    public OperationResult DepositCash(Map<Banknote, Integer> banknotes) {
        // Очистка стэка после последней операции
        commandHistory.clear();
        try {
            StringBuilder resultBuilder = new StringBuilder("Внесено наличных:\n");
            int totalAmount = 0;
            for (Map.Entry<Banknote, Integer> entry : banknotes.entrySet()) {
                // Тут смысла в Chain of Responsibility не увидел.
                // Все равно все деньги надо разложить по ячейкам
                // Перебор показался удовлетворительным вариантом
                int nominal = entry.getKey().getNominal();
                int amount  = entry.getValue();
                putCashIntoCell(entry.getKey(), amount);
                resultBuilder.append("Номинал "+nominal+": "+amount+" шт.");
                totalAmount =+ nominal * amount;
            }
            resultBuilder.append("Общая сумма: "+totalAmount);
            return new OperationResultImpl(resultBuilder.toString());
        }
        catch(Throwable e) {
            undo();
            return new OperationResultImpl("Ошибка: "+e.getMessage());
        }
    }

    public Map<Banknote, Integer> cashEntering() {
        Map<Banknote, Integer> enteredBanknotes = new HashMap<>();
        int enteredNominal = 0;
        int amount = 0;
        ioService.printLn("Внесите деньги:");
        do {
            ioService.print("Номинал (0-выход): ");
            try {
                enteredNominal = ioService.readInt();
            } catch (IllegalArgumentException e) {
                ioService.printLn("Ошибка ввода.");
                continue;
            }

            if (enteredNominal == 0) break;

            NOMINAL nominal;
            try {
                nominal = NOMINAL.valueOf(enteredNominal);
            } catch (BadNominalException e) {
                ioService.printLn("Указанный номинал " + enteredNominal + " не обрабатывается.");
                continue;
            }

            ioService.print("Количество купюр: ");
            try {
                amount = ioService.readInt();
                if (amount > 0) {
                    enteredBanknotes.put(new BanknoteImpl(enteredNominal), amount);
                } else {
                    ioService.printLn("Введено ошибочное количество купюр: " + amount);
                }
            } catch (IllegalArgumentException e) {
                ioService.printLn("Ошибка ввода.");
                continue;
            }
        } while (true);
        return enteredBanknotes;
    }

    public void printATMState() {
        printATMState("");
    }

    public void printATMState(String msg) {
        int totalAmount = 0;
        Collections.sort(atmCellHandlers, Collections.reverseOrder(new ATMCellHandlerComparator()));

        System.out.println("-------------------------");
        System.out.println("-- Состояние банкомата");
        if(!msg.isBlank() && !msg.isEmpty()) {
            System.out.println("-- "+msg);
        }
        System.out.println("-------------------------");
        for (ATMCellHandler handler: atmCellHandlers) {
            ioService.printLn("Ячейка (номинал: " + handler.getNominal() +
                    "): " + handler.getBanknotesAmount());
            totalAmount += handler.getNominal() * handler.getBanknotesAmount();
        }
        System.out.println("-------------------------\n");
    }
}
