package ru.otus.frontend.messagesystem;

import ru.otus.common.core.messagetypes.OperationStatusMsgData;
import ru.otus.common.core.messagetypes.UserListMsgData;
import ru.otus.common.core.messagetypes.UserMsgData;
import ru.otus.common.core.model.SearchForm;
import ru.otus.common.core.model.User;
import ru.otus.messagesystem.client.MessageCallback;


public interface FrontendService {

    void getAllUsers(SearchForm searchForm, MessageCallback<UserListMsgData> dataConsumer);

    void getUser(long userId, MessageCallback<UserMsgData> dataConsumer);

    void saveUser(User user, MessageCallback<OperationStatusMsgData> dataConsumer);

    void deleteUser(long userId, MessageCallback<OperationStatusMsgData> dataConsumer);
}
