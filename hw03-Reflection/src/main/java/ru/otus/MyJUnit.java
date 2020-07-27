package ru.otus;

import org.junit.jupiter.api.DisplayName;
import ru.otus.annotations.Test;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyJUnit {

    private int failedTests = 0;
    private int succedTests = 0;

    public static void main(String[] args) {
        MyJUnit myJUnit = new MyJUnit();
        myJUnit.run("ru.otus.DIYArrayListTest");
    }

    public MyJUnit() {
    }

    public void run(String testClassName) {
        Class<?> clazz;
        try {
            clazz = Class.forName(testClassName);
        } catch (java.lang.Exception e) {
            System.out.println("Тестовый класс " + testClassName + " не найден.");
            return;
        }

        cleanStatistics();

        Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
        final List<Object> params = new ArrayList<Object>();

        List<Method> beforeMethods = getMethodsAnnotatedWith(clazz, Before.class);
        List<Method> testMethods = getMethodsAnnotatedWith(clazz, Test.class);
        List<Method> afterMethods = getMethodsAnnotatedWith(clazz, After.class);

        // Run each test
        testMethods.stream().forEach(
                testMethod -> {
                    Object obj;
                    // Для каждого теста новый экземпляр
                    try {
                        obj = declaredConstructors[0].newInstance();
                    } catch (Exception e) {
                        System.out.println("Тестовый класс " + testClassName + ": " + e.getMessage());
                        e.printStackTrace();
                        return;
                    }

                    if (runTest(obj, beforeMethods, testMethod, afterMethods)) {
                        System.out.println("SUCCESS");
                        succedTests++;
                    } else {
                        System.out.println("FAIL");
                        failedTests++;
                    }
                }
        );

        printStatistics();
    }

    private void cleanStatistics() {
        failedTests = 0;
        succedTests = 0;
    }

    private void printStatistics() {
        System.out.println(
                "\nTotals: " + (succedTests + failedTests)
                        + "\nSucceed: " + succedTests
                        + "\nFailed: " + failedTests
        );
    }

    private static List<Method> getMethodsAnnotatedWith(final Class<?> clazz, final Class<? extends Annotation> annotation) {
        final List<Method> methods = new ArrayList<Method>();
        for (final Method method : clazz.getDeclaredMethods()) {
            if (method.isAnnotationPresent(annotation)) {
                methods.add(method);
            }
        }
        return methods;
    }

    private boolean runTest(Object obj, List<Method> beforeMethods, Method method, List<Method> afterMethods) {
        try {
            Object result;

            if (method.isAnnotationPresent(DisplayName.class)) {
                DisplayName displayName = (DisplayName) method.getAnnotation(DisplayName.class);
                System.out.println(displayName.value());
            } else {
                System.out.println(method.getName());
            }

            // Before method processing
            for (Method beforeMethod : beforeMethods) {
                result = beforeMethod.invoke(obj);
            }

            // Test method processing
            result = method.invoke(obj);

            //  After method processing
            for (Method afterMethod : afterMethods) {
                result = afterMethod.invoke(obj);
            }

            return true; // The test has passed
        } catch (Exception e) {
            return false; // The test has failed
        }
    }
}
