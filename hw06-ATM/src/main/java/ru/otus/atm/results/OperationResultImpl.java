package ru.otus.atm.results;

public class OperationResultImpl implements OperationResult {
    private String result;

    public OperationResultImpl(String result) {
        this.result = result;
    }

    @Override
    public String getResult() {
        return result;
    }
}
