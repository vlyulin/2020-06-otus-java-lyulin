package ru.otus.frontend;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException {
        MyJUnit myJUnit = new MyJUnit();
        List<TestExecutionResult> results = myJUnit.run("ru.otus.DIYArrayListTest");
        printStatistics(results);
    }

    private static void printStatistics(List<TestExecutionResult> results) {
        int failedTests = 0;
        int succedTests = 0;

        for (TestExecutionResult result: results) {
            System.out.println(result);
            if (result.getResult()) {
                succedTests++;
            } else {
                failedTests++;
            }
        }

        System.out.println(
                "\nResults:\n\nTotals: " + (succedTests + failedTests)
                        + "\nSucceed: " + succedTests
                        + "\nFailed: " + failedTests
        );
    }
}
