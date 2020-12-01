package ru.otus.dbserver.messagesystem;

import ro.otus.common.core.messagetypes.DeleteUserMsgData;
import ro.otus.common.core.messagetypes.OperationStatusMsgData;
import ro.otus.common.core.messagetypes.SearchFormMsgData;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

public class DeleteUserRequestHandler implements RequestHandler<SearchFormMsgData> {
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    private final UserDao userDao;

    public DeleteUserRequestHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        OperationStatusMsgData operationStatusMsgData;
        try {
            DeleteUserMsgData deleteUserMsgData = MessageHelper.getPayload(msg);
            if(deleteUserMsgData != null) {
                long userId = deleteUserMsgData.getUserId();
                if(userId != 0) {
                    userDao.deleteById(userId);
                }
            }
            operationStatusMsgData = new OperationStatusMsgData(SUCCESS, new ArrayList<>());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            operationStatusMsgData = new OperationStatusMsgData(ERROR, Arrays.asList(e.getMessage()));
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, operationStatusMsgData, MessageType.OPERATION_STATUS));
    }
}
