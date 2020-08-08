package ru.otus.asserts;

public class DoubleAssert {
    private Double actual;

    public DoubleAssert(Double actual) {
        this.actual = actual;
    }

    public DoubleAssert isEqualTo(Double expected) throws Exception {
        if (actual.compareTo(expected) != 0) {
            throw new Exception(actual + " not equal " + expected);
        }
        return this;
    }
}
