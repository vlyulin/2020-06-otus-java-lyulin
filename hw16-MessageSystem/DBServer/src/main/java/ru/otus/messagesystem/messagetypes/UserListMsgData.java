package ru.otus.messagesystem.messagetypes;

import ru.otus.core.model.User;
import ru.otus.messagesystem.client.ResultDataType;

import java.io.Serializable;
import java.util.List;

public class UserListMsgData extends ResultDataType implements Serializable {

    private final List<User> userList;

    public UserListMsgData(List<User> userList) {
        this.userList = userList;
    }

    public List<User> getData() {
        return userList;
    }

    @Override
    public String toString() {
        return "UserListMsgData{" +
                "userList=" + userList +
                '}';
    }
}
