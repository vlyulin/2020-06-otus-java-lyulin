package ru.otus.frontend;

public class TestExecutionResult {
    private String testName;
    private boolean result;
    private Throwable exception;

    public TestExecutionResult(String testName, boolean result) {
        this.testName = testName;
        this.result = result;
    }

    public TestExecutionResult(String testName, boolean result, Throwable exception) {
        this.testName = testName;
        this.result = result;
        this.exception = exception;
    }

    public String getTestName() {
        return testName;
    }

    public boolean getResult() {
        return result;
    }

    @Override
    public String toString() {
        return testName + ": "
                + ((result) ? "SUCCESS" : "FAIL\n\t" + exception.getMessage());
    }
}
