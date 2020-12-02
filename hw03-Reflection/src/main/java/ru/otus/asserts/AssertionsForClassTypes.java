package ru.otus.asserts;


public class AssertionsForClassTypes {
    public static IntegerAssert assertThat(int actual) {
        return new IntegerAssert(actual);
    }

    public static DoubleAssert assertThat(double actual) {
        return new DoubleAssert(actual);
    }

    public static BooleanAssert assertThat(boolean actual) {
        return new BooleanAssert(actual);
    }
}
