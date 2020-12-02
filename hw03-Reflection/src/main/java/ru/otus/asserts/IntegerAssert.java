package ru.otus.asserts;

import ru.otus.exceptions.MyJUnitAssertException;

public class IntegerAssert {
    private Integer actual;

    public IntegerAssert(Integer actual) {
        this.actual = actual;
    }

    public IntegerAssert isEqualTo(Integer expected) {
        if(actual.compareTo(expected) != 0) {
            throw new MyJUnitAssertException(actual + " not equal " + expected);
        }
        return this;
    }
}
