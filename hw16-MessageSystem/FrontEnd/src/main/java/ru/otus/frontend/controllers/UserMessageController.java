package ru.otus.frontend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.frontend.core.model.SearchForm;
import ru.otus.frontend.core.model.User;
import ru.otus.messagesystem.FrontendService;

@Controller
public class UserMessageController {
    private static final Logger logger = LoggerFactory.getLogger(UserMessageController.class);
    public static final String SUCCESS = "SUCCESS";
    private final SimpMessagingTemplate template;
    private final FrontendService frontendService;

    public UserMessageController(SimpMessagingTemplate template, FrontendService frontendService) {
        this.template = template;
        this.frontendService = frontendService;
    }

    @MessageMapping("/user/list")
    public void getAllUsers() {
        logger.info("getAllUsers()");
        frontendService.getAllUsers(new SearchForm(), userListData -> {
            this.template.convertAndSend("/topic/userList",
                    userListData.getData());
            logger.info("send list of users : {}", userListData.getData());
        });
    }

    @MessageMapping("/user/search")
    public void userSearchView(SearchForm searchForm) {
        logger.info("getAllUsers()");
        frontendService.getAllUsers(searchForm, userListData -> {
            this.template.convertAndSend("/topic/userList",
                    userListData.getData());
            logger.info("send user list founded : {}", userListData.getData());
        });
    }

    @MessageMapping("/user/{userId}")
    public void getUser(@DestinationVariable long userId) {
        logger.info("getUser(userId = " + userId + ")");
        frontendService.getUser(userId, userData -> {
            this.template.convertAndSend("/topic/getUser", userData.getData());
            logger.info("send user : {}", userData.getData());
        });
    }

    @MessageMapping("/user/save")
    public void saveUser(User user) {
        logger.info("saveUser: user = " + user.toString());
        frontendService.saveUser(user, (statusMsg) -> {
            this.template.convertAndSend("/topic/saveUserOperationStatus", statusMsg);
            logger.info("send " + statusMsg.getStatus() + " to saveUserOperationStatus");
        });
    }

    @MessageMapping("user/delete/{userId}")
    public void deleteUser(@DestinationVariable long userId) {
        logger.info("deleteUser: userId = " + userId);
        frontendService.deleteUser(userId, (statusMsg) -> {
            this.template.convertAndSend("/topic/operationStatus", statusMsg);
            logger.info("send " + statusMsg.getStatus() + " to saveUserOperationStatus");
        });
    }
}
