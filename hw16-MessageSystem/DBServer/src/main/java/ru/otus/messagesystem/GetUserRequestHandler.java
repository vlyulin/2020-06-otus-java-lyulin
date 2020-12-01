package ru.otus.messagesystem;

import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.User;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.messagetypes.UserMsgData;
import ru.otus.messagesystem.messagetypes.SearchFormMsgData;

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
        return Optional.of(MessageBuilder.buildReplyMessage(msg, userMsgData));
    }
}
