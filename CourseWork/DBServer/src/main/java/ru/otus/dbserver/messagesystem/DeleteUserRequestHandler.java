package ru.otus.dbserver.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.core.messagetypes.DeleteUserMsgData;
import ru.otus.common.core.messagetypes.OperationStatusMsgData;
import ru.otus.common.core.messagetypes.SearchFormMsgData;
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

    private static final Logger logger = LoggerFactory.getLogger(DeleteUserRequestHandler.class);
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
            operationStatusMsgData = new OperationStatusMsgData(OperationStatusMsgData.ResponseStatus.SUCCESS, new ArrayList<>());

        } catch (Exception e) {
            logger.info(e.getMessage());
            operationStatusMsgData = new OperationStatusMsgData(OperationStatusMsgData.ResponseStatus.ERROR, Arrays.asList(e.getMessage()));
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, operationStatusMsgData, MessageType.OPERATION_STATUS));
    }
}
