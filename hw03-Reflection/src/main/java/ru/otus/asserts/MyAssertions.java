package ru.otus.asserts;

public class MyAssertions {
    public static IntegerAssert assertThat(int actual) {
        return AssertionsForClassTypes.assertThat(actual);
    }
    public static DoubleAssert assertThat(double actual) {
        return AssertionsForClassTypes.assertThat(actual);
    }
    public static BooleanAssert assertThat(boolean actual) {
        return AssertionsForClassTypes.assertThat(actual);
    }

    public static void assertTrue(Boolean actual) throws Exception {
        if(!actual) {
            throw new Exception("Not true.");
        }
    }

    public static void assertEquals(Object expected, Object actual) throws Exception {
        if(!objectsAreEqual(expected,actual)) {
            throw new Exception("Objects are not equil.");
        }
    }

    static boolean objectsAreEqual(Object obj1, Object obj2) {
        if (obj1 == null) {
            return (obj2 == null);
        }
        return obj1.equals(obj2);
    }

    public static <T> void assertThrows(Class<T> expectedType, Runnable executable) throws Exception {
        try {
            executable.run();
        }
        catch (Throwable actualException) {
            if (expectedType.isInstance(actualException)) {
                return;
            }
            else {
                throw new Exception("Unexpected exception type thrown");
            }
        }
        throw new Exception("No exception happened.");
    }
}
