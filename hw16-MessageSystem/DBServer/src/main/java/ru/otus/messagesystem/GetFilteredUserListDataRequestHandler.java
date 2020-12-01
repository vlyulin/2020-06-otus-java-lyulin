package ru.otus.messagesystem;

import ru.otus.frontend.core.dao.UserDao;
import ru.otus.frontend.core.model.SearchForm;
import ru.otus.frontend.core.model.User;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.messagesystem.messagetypes.SearchFormMsgData;
import ru.otus.messagesystem.messagetypes.UserListMsgData;

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
