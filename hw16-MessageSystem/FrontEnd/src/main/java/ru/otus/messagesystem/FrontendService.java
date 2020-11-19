package ru.otus.messagesystem;

import ru.otus.core.model.SearchForm;
import ru.otus.core.model.User;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.messagetypes.OperationStatusMsgData;
import ru.otus.messagesystem.messagetypes.UserListMsgData;
import ru.otus.messagesystem.messagetypes.UserMsgData;

public interface FrontendService {

    void getAllUsers(SearchForm searchForm, MessageCallback<UserListMsgData> dataConsumer);

    void getUser(long userId, MessageCallback<UserMsgData> dataConsumer);

    void saveUser(User user, MessageCallback<OperationStatusMsgData> dataConsumer);

    void deleteUser(long userId, MessageCallback<OperationStatusMsgData> dataConsumer);
}
