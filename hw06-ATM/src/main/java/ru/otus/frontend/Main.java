package ru.otus.frontend;

import ru.otus.frontend.atm.ATM;
import ru.otus.frontend.atm.ATMImpl;
import ru.otus.frontend.atm.cellhandlers.ATMCellHandler;
import ru.otus.frontend.atm.cellhandlers.CashATMCellHandler;
import ru.otus.frontend.atm.strategies.DescendingWithdrawStrategyImpl;
import ru.otus.frontend.atm.strategies.SmallBillsWithdrawStrategyImpl;
import ru.otus.frontend.banknotes.Banknote;
import ru.otus.frontend.banknotes.BanknoteImpl;
import ru.otus.frontend.banknotes.NOMINAL;
import ru.otus.frontend.atm.exceptions.ATMNotEnoughMoneyException;
import ru.otus.frontend.ioservice.ConsoleIOService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {

        // Формируем кассеты для хранения банкнот, по одной для каждого номинала
        List<ATMCellHandler> atmCellHandlers = new ArrayList<>();
        for (NOMINAL nominal : NOMINAL.values()) {
            CashATMCellHandler cashATMCellHandler = new CashATMCellHandler(new BanknoteImpl(nominal.getNominal()), 100);
            atmCellHandlers.add(cashATMCellHandler);
        }

        // Собираем банкомат нашей мечты
        ATM atm = new ATMImpl()
                .setNominals(NOMINAL.values())
                .setIOService(new ConsoleIOService())
                .setCellHandlers(atmCellHandlers) // вставляем кассеты с деньгами
                .setWithdrawStrategy(new DescendingWithdrawStrategyImpl());

        try {
            System.out.println("Запрашиваем выдачу 147750.");
            Map<Banknote, Integer> issuedBanknotes = atm.withdrawCash(147750);
            printIssuedBanknotes(issuedBanknotes);
        } catch (ATMNotEnoughMoneyException atmNotEnoughMoneyException) {
            System.out.println(atmNotEnoughMoneyException.getMessage());
        }

        atm.printATMState();

        try {
            System.out.println("Запрашиваем выдачу 49.");
            Map<Banknote, Integer> issuedBanknotes = atm.withdrawCash(49);
            printIssuedBanknotes(issuedBanknotes);
        } catch (ATMNotEnoughMoneyException atmNotEnoughMoneyException) {
            System.out.println(atmNotEnoughMoneyException.getMessage());
        }

        atm.printATMState();

        // Перестраиваем банкомат на выдачу мелкими купюрами
        ((ATMImpl) atm).setWithdrawStrategy(new SmallBillsWithdrawStrategyImpl());

        try {
            System.out.println("Запрашиваем выдачу 117550 мелкими купюрами.");
            Map<Banknote, Integer> issuedBanknotes = atm.withdrawCash(117550);
            printIssuedBanknotes(issuedBanknotes);
        } catch (ATMNotEnoughMoneyException atmNotEnoughMoneyException) {
            System.out.println(atmNotEnoughMoneyException.getMessage());
        }

        atm.printATMState();

        ((ATMImpl) atm).printATMState("До выдвчи 500000");
        try {
            System.out.println("Запрашиваем выдачу 500000, это большей чем есть в банкомате.");
            Map<Banknote, Integer> issuedBanknotes = atm.withdrawCash(500000);
            printIssuedBanknotes(issuedBanknotes);
        } catch (ATMNotEnoughMoneyException atmNotEnoughMoneyException) {
            System.out.println(atmNotEnoughMoneyException.getMessage());
        }
        ((ATMImpl) atm).printATMState("После ошибки");

        // Внесение денежных средств
        Map<Banknote, Integer> enteredBanknotes = ((ATMImpl) atm).cashEntering();
        atm.DepositCash(enteredBanknotes);
        ((ATMImpl) atm).printATMState("После внесения денежных средств");
    }

    public static void printIssuedBanknotes(Map<Banknote, Integer> issuedBanknotes) {
        System.out.println("-------------------------");
        System.out.println("-- Выданные банкноты:");
        System.out.println("-------------------------");
        issuedBanknotes.entrySet().stream()
                .sorted(Map.Entry.<Banknote, Integer>comparingByKey().reversed())
                .forEach(entry ->
                        System.out.println("Номинал: " + entry.getKey().getNominal()
                                + " количество: " + entry.getValue()));
        System.out.println("-------------------------\n");
    }
}
