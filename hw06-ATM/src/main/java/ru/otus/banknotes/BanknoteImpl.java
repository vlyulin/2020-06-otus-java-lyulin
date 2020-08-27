package ru.otus.banknotes;

import ru.otus.banknotes.exceptions.BanknoteException;

public class BanknoteImpl implements Banknote {

    private int nominal = 1;

    public BanknoteImpl(int nominal) {
        if (nominal <= 0) {
            throw new BanknoteException("Указан ошибочный номинал " + nominal + " меньше нуля.");
        }
        this.nominal = nominal;
    }

    @Override
    public int getNominal() {
        return nominal;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof BanknoteImpl))
            return false;
        BanknoteImpl banknote = (BanknoteImpl)obj;
        return this.nominal == banknote.getNominal();
    }

    @Override
    public int compareTo(Banknote otherBanknote) {
        return Integer.compare(nominal, otherBanknote.getNominal());
    }

    @Override
    public Banknote clone() {
        return new BanknoteImpl(this.nominal);
    }
}
