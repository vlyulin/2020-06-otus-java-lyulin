package ru.otus.frontend.asserts;

import ru.otus.frontend.exceptions.MyJUnitAssertException;

public class DoubleAssert {
    private Double actual;

    public DoubleAssert(Double actual) {
        this.actual = actual;
    }

    public DoubleAssert isEqualTo(Double expected) {
        if (actual.compareTo(expected) != 0) {
            throw new MyJUnitAssertException(actual + " not equal " + expected);
        }
        return this;
    }
}
