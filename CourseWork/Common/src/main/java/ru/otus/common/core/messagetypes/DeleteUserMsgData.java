package ru.otus.common.core.messagetypes;

import ru.otus.messagesystem.client.ResultDataType;

import java.io.Serializable;

public class DeleteUserMsgData extends ResultDataType implements Serializable {

    private final long userId;

    public DeleteUserMsgData(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    @Override
    public String toString() {
        return "DeleteUserMsgData{" +
                "userId=" + userId +
                '}';
    }
}
