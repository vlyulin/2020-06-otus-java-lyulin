package ru.otus.frontend.services;

import ru.otus.frontend.model.Equation;

import java.util.List;

public interface EquationPreparer {
    List<Equation> prepareEquationsFor(int base);
}
