package ru.otus;

import ru.otus.annotations.DisplayName;
import ru.otus.annotations.Test;
import ru.otus.annotations.After;
import ru.otus.annotations.Before;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyJUnit {

    public MyJUnit() {
    }

    public List<TestExecutionResult> run(String testClassName) throws ClassNotFoundException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Class<?> clazz = Class.forName(testClassName);

        Constructor<?> constructor = clazz.getDeclaredConstructors()[0];
        final List<Object> params = new ArrayList<Object>();

        List<Method> beforeMethods = getMethodsAnnotatedWith(clazz, Before.class);
        List<Method> testMethods = getMethodsAnnotatedWith(clazz, Test.class);
        List<Method> afterMethods = getMethodsAnnotatedWith(clazz, After.class);

        // Run each test
        List<TestExecutionResult> testResults = new ArrayList<>();
        for (Method testMethod : testMethods) {
            // Для каждого теста новый экземпляр
            Object obj = constructor.newInstance();
            testResults.add(runTest(obj, beforeMethods, testMethod, afterMethods));
        }
        return testResults;
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

    private TestExecutionResult runTest(Object obj, List<Method> beforeMethods, Method method, List<Method> afterMethods) {
        String testName = "Not determined";

        try {
            Object result;

            if (method.isAnnotationPresent(DisplayName.class)) {
                DisplayName displayName = (DisplayName) method.getAnnotation(DisplayName.class);
                testName = displayName.value();
            } else {
                testName = method.getName();
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

            return new TestExecutionResult(testName, true); // The test has passed
        } catch (Exception e) {
            return new TestExecutionResult(testName, false); // The test has failed
        }
    }
}
