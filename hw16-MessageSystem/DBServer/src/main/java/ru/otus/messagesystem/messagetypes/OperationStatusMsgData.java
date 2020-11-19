package ru.otus.messagesystem.messagetypes;

import ru.otus.messagesystem.client.ResultDataType;

import java.io.Serializable;
import java.util.List;

public class OperationStatusMsgData extends ResultDataType implements Serializable {
    private final String status;
    private final List<String> constraintViolations;

    public OperationStatusMsgData(String status, List<String> constraintViolations) {
        this.status = status;
        this.constraintViolations = constraintViolations;
    }

    public String getStatus() {
        return status;
    }

    public List<String> getConstraintViolations() {
        return constraintViolations;
    }
}
