package ru.otus.frontend.banknotes;

import ru.otus.frontend.banknotes.exceptions.BadNominalException;

public enum NOMINAL {
    B5000(5000), B1000(1000), B500(500), B200(200), B100(100), B50(50);
    private int nominal;

    NOMINAL(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return this.nominal;
    }

    public static NOMINAL valueOf(int n) {
        for (NOMINAL nominal : values()) {
            if (nominal.getNominal() == n) {
                return nominal;
            }
        }
        throw new BadNominalException("Номинал " + n + " не обрабатывается");
    }
}
