package ru.otus;

public class TestExecutionResult {
    private String testName;
    private boolean result;

    public TestExecutionResult(String testName, boolean result) {
        this.testName = testName;
        this.result = result;
    }

    public String getTestName() {
        return testName;
    }

    public boolean getResult() {
        return result;
    }

    @Override
    public String toString() {
        return testName + ": " + ((result) ? "SUCCESS" : "FAIL");
    }
}
