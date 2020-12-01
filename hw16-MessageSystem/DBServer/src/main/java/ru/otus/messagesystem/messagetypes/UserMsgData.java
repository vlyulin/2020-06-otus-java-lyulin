package ru.otus.messagesystem.messagetypes;

import ru.otus.frontend.core.model.User;
import ru.otus.messagesystem.client.ResultDataType;

import java.io.Serializable;

public class UserMsgData extends ResultDataType implements Serializable {

    private final User user;

    public UserMsgData(User user) {
        this.user = user;
    }

    public User getData() {
        return user;
    }

    @Override
    public String toString() {
        return "UserMsgData{" +
                "user =" + user +
                '}';
    }
}
