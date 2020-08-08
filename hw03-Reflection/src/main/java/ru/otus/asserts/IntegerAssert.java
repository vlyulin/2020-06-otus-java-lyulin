package ru.otus.asserts;

public class IntegerAssert {
    private Integer actual;

    public IntegerAssert(Integer actual) {
        this.actual = actual;
    }

    public IntegerAssert isEqualTo(Integer expected) throws Exception {
        if(actual.compareTo(expected) != 0) {
            throw new Exception(actual + " not equal " + expected);
        }
        return this;
    }
}
