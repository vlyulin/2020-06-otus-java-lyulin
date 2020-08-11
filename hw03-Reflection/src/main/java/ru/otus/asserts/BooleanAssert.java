package ru.otus.asserts;

import ru.otus.exceptions.MyJUnitAssertException;

public class BooleanAssert {
    private Boolean actual;

    public BooleanAssert(Boolean actual) {
        this.actual = actual;
    }

    public BooleanAssert isTrue() {
        return isEqualTo(true);
    }

    public BooleanAssert isFalse() {
        return isEqualTo(false);
    }

    public BooleanAssert isEqualTo(Boolean expected) {
        if (actual.compareTo(expected) != 0) {
            throw new MyJUnitAssertException(actual + " not equal " + expected);
        }
        return this;
    }
}