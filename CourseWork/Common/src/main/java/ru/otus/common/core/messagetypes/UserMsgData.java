package ru.otus.common.core.messagetypes;

import ru.otus.common.core.model.User;
import ru.otus.messagesystem.client.ResultDataType;

public class UserMsgData extends ResultDataType /*implements Serializable*/ {

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
