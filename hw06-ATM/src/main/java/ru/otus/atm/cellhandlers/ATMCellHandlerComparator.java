package ru.otus.atm.cellhandlers;

import java.util.Comparator;

public class ATMCellHandlerComparator implements Comparator<ATMCellHandler> {
    @Override
    public int compare(ATMCellHandler cell1, ATMCellHandler cell2) {
        return Integer.compare(cell1.getNominal(),cell2.getNominal());
    }
}
