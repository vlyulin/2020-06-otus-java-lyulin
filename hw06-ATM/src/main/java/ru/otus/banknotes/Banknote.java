package ru.otus.banknotes;

public interface Banknote extends Comparable<Banknote> {
    int getNominal();
    Banknote clone();
}
