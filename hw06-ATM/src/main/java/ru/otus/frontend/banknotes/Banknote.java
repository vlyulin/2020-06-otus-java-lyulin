package ru.otus.frontend.banknotes;

public interface Banknote extends Comparable<Banknote> {
    int getNominal();
    Banknote clone();
}
