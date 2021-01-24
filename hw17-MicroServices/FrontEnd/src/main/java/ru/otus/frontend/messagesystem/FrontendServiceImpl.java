package ru.otus.frontend.messagesystem;

import org.springframework.beans.factory.annotation.Qualifier;
import ro.otus.common.core.messagetypes.*;
import ro.otus.common.core.model.SearchForm;
import ro.otus.common.core.model.User;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;


public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(@Qualifier("frontendMsClient") MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getAllUsers(SearchForm searchForm, MessageCallback<UserListMsgData> dataConsumer) {
        Message outMsg = msClient.produceMessage(
                databaseServiceClientName,
                new SearchFormMsgData(searchForm),
                MessageType.GET_FILTERED_USERS,
                dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void getUser(long userId, MessageCallback<UserMsgData> dataConsumer) {
        Message outMsg = msClient.produceMessage(
                databaseServiceClientName,
                Long.valueOf(userId),
                MessageType.GET_USER,
                dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void saveUser(User user, MessageCallback<OperationStatusMsgData> dataConsumer) {
        Message outMsg = msClient.produceMessage(
                databaseServiceClientName,
                new UserMsgData(user),
                MessageType.SAVE_USER,
                dataConsumer);
        msClient.sendMessage(outMsg);
    }

    @Override
    public void deleteUser(long userId, MessageCallback<OperationStatusMsgData> dataConsumer) {
        Message outMsg = msClient.produceMessage(
                databaseServiceClientName,
                new DeleteUserMsgData(userId),
                MessageType.DELETE_USER,
                dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
