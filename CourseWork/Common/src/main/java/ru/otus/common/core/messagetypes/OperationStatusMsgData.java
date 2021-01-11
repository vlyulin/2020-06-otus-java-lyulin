package ru.otus.common.core.messagetypes;

import ru.otus.messagesystem.client.ResultDataType;

import java.io.Serializable;
import java.util.List;

public class OperationStatusMsgData extends ResultDataType {

    public enum ResponseStatus {
        SUCCESS("SUCCESS"),
        ERROR("ERROR");

        private String status;

        ResponseStatus(String status) {
            this.status = status;
        }
        @Override
        public String toString() {
            return status;
        }
    };

    private final ResponseStatus status;
    private final List<String> constraintViolations;

    public OperationStatusMsgData(ResponseStatus status, List<String> constraintViolations) {
        this.status = status;
        this.constraintViolations = constraintViolations;
    }

    public String getStatus() {
        return status.toString();
    }

    public List<String> getConstraintViolations() {
        return constraintViolations;
    }
}
