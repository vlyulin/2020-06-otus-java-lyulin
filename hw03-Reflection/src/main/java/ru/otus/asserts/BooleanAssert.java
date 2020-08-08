package ru.otus.asserts;

public class BooleanAssert {
    private Boolean actual;

    public BooleanAssert(Boolean actual) {
        this.actual = actual;
    }

    public BooleanAssert isTrue() throws Exception {
        return isEqualTo(true);
    }

    public BooleanAssert isFalse() throws Exception {
        return isEqualTo(false);
    }

    public BooleanAssert isEqualTo(Boolean expected) throws Exception {
        if (actual.compareTo(expected) != 0) {
            throw new Exception(actual + " not equal " + expected);
        }
        return this;
    }
}