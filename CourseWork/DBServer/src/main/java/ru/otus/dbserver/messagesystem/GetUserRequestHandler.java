package ru.otus.dbserver.messagesystem;

import ru.otus.common.core.messagetypes.SearchFormMsgData;
import ru.otus.common.core.messagetypes.UserMsgData;
import ru.otus.common.core.model.User;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.Optional;

public class GetUserRequestHandler implements RequestHandler<SearchFormMsgData> {
    private final UserDao userDao;

    public GetUserRequestHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        Long userId = MessageHelper.getPayload(msg);
        UserMsgData userMsgData = new UserMsgData(new User());
        if(userId != null) {
            Optional<User> userOpt = userDao.findById(userId.longValue());
            if(userOpt.isPresent()) userMsgData = new UserMsgData(userOpt.get());
        }

        Message outMsg = MessageBuilder.buildReplyMessage(msg, userMsgData);
        return Optional.of(outMsg);
    }
}
