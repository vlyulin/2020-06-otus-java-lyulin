package ru.otus.messagesystem;

import ru.otus.core.dao.UserDao;
import ru.otus.core.model.User;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messagesystem.messagetypes.OperationStatusMsgData;
import ru.otus.messagesystem.messagetypes.SearchFormMsgData;
import ru.otus.messagesystem.messagetypes.UserMsgData;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

public class SaveUserRequestHandler implements RequestHandler<SearchFormMsgData> {
    // TODO: Куда бы его засунуть, чтобы был един и для фронта, и для бэкенда
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
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
            operationStatusMsgData = new OperationStatusMsgData(SUCCESS, new ArrayList<>());

        } catch( ConstraintViolationException ex) {
            Set<ConstraintViolation<?>> constraintViolationSet = ex.getConstraintViolations();
            ArrayList<String> errors = new ArrayList<>();
            for (ConstraintViolation<?> constraintViolation: constraintViolationSet) {
                errors.add(constraintViolation.getMessage());
            }
            operationStatusMsgData = new OperationStatusMsgData(ERROR, errors);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            operationStatusMsgData = new OperationStatusMsgData(ERROR, Arrays.asList(e.getMessage()));
        }
        return Optional.of(MessageBuilder.buildReplyMessage(msg, operationStatusMsgData, MessageType.OPERATION_STATUS));
    }
}
