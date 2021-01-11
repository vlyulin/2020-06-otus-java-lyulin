package ru.otus.dbserver.messagesystem;

import ru.otus.common.core.messagetypes.SearchFormMsgData;
import ru.otus.common.core.messagetypes.UserListMsgData;
import ru.otus.common.core.model.SearchForm;
import ru.otus.common.core.model.User;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;

import java.util.List;
import java.util.Optional;

public class GetFilteredUserListDataRequestHandler implements RequestHandler<SearchFormMsgData> {
    private final UserDao userDao;

    public GetFilteredUserListDataRequestHandler(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        SearchFormMsgData searchFormMsgData = MessageHelper.getPayload(msg);
        SearchForm searchForm = (searchFormMsgData == null)? new SearchForm(): searchFormMsgData.getSearchForm();
        List<User> userList = userDao.findByMask(
                String.valueOf(searchForm.getId()),
                searchForm.getName(),
                searchForm.getLogin());
        UserListMsgData userListMsgData = new UserListMsgData(userList);

        return Optional.of(MessageBuilder.buildReplyMessage(msg, userListMsgData));
    }
}
