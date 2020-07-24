package ru.otus;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MyJUnit {

    private int failedTests = 0;
    private int successTests = 0;

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

        System.out.println("simpleName:" + clazz.getSimpleName());
        System.out.println("canonicalName:" + clazz.getCanonicalName());

        System.out.println("@Before:");
        List<Method> beforeMethods = getMethodsAnnotatedWith(clazz, BeforeEach.class);
        beforeMethods.stream().forEach(method -> System.out.println(method.getName()));

        System.out.println("@Test");
        List<Method> testMethods = getMethodsAnnotatedWith(clazz, Test.class);
        testMethods.stream().forEach(method -> System.out.println(method.getName()));

        System.out.println("@After");
        List<Method> afterMethods = getMethodsAnnotatedWith(clazz, AfterEach.class);

        testMethods.stream().forEach(testMethod -> runTest(beforeMethods, testMethod, afterMethods));
    }

    private void cleanStatistics() {
        failedTests = 0;
        successTests = 0;
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

    private void runTest(List<Method> beforeMethods, Method method, List<Method> afterMethods) {
        System.out.println("run " + method.getName());
        if (method.isAnnotationPresent(DisplayName.class)) {
            Annotation annotation = method.getAnnotation(DisplayName.class);
            System.out.println(annotation.toString());
        }
        try {
            for (Method beforeMethod : beforeMethods) {
                System.out.println("Try to run: "+beforeMethod.getName());
                var result = beforeMethod.invoke(null);
            }
            successTests++;
        } catch (Exception e) {
            failedTests++;
        }
    }
}
