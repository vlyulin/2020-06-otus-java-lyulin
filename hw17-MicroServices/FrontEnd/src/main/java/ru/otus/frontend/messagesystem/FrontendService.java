package ru.otus.frontend.messagesystem;

import ro.otus.common.core.messagetypes.OperationStatusMsgData;
import ro.otus.common.core.messagetypes.UserListMsgData;
import ro.otus.common.core.messagetypes.UserMsgData;
import ro.otus.common.core.model.SearchForm;
import ro.otus.common.core.model.User;
import ru.otus.messagesystem.client.MessageCallback;


public interface FrontendService {

    void getAllUsers(SearchForm searchForm, MessageCallback<UserListMsgData> dataConsumer);

    void getUser(long userId, MessageCallback<UserMsgData> dataConsumer);

    void saveUser(User user, MessageCallback<OperationStatusMsgData> dataConsumer);

    void deleteUser(long userId, MessageCallback<OperationStatusMsgData> dataConsumer);
}
