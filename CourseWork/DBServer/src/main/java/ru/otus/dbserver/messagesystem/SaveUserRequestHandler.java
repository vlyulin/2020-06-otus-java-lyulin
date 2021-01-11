package ru.otus.dbserver.messagesystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.common.core.messagetypes.OperationStatusMsgData;
import ru.otus.common.core.messagetypes.SearchFormMsgData;
import ru.otus.common.core.messagetypes.UserMsgData;
import ru.otus.common.core.model.User;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class SaveUserRequestHandler implements RequestHandler<SearchFormMsgData> {

    private static final Logger logger = LoggerFactory.getLogger(SaveUserRequestHandler.class);
    private final UserDao userDao;

    public SaveUserRequestHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        OperationStatusMsgData operationStatusMsgData;
        try {
            UserMsgData userMsgData = MessageHelper.getPayload(msg);
            if(userMsgData != null) {
                User user = userMsgData.getData();
                if(user != null) {
                    userDao.updateUser(user);
                }
            }
            operationStatusMsgData = new OperationStatusMsgData(
                    OperationStatusMsgData.ResponseStatus.SUCCESS,
                    new ArrayList<>());

        } catch( ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> constraintViolationSet = ex.getConstraintViolations();
            ArrayList<String> errors = new ArrayList<>();
            for (ConstraintViolation<?> constraintViolation: constraintViolationSet) {
                errors.add(constraintViolation.getMessage());
            }
            operationStatusMsgData = new OperationStatusMsgData(OperationStatusMsgData.ResponseStatus.ERROR, errors);
        }
        catch (Exception e) {
            logger.info(e.getMessage());
            operationStatusMsgData = new OperationStatusMsgData(OperationStatusMsgData.ResponseStatus.ERROR, Arrays.asList(e.getMessage()));
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, operationStatusMsgData, MessageType.OPERATION_STATUS));
    }
}
