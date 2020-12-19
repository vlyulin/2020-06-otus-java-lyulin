package ru.otus.frontend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

// https://stackoverflow.com/questions/24756312/how-can-i-send-a-message-on-connect-event-sockjs-stomp-spring
// Отправка списка пользователей на присоединение клиента
//@Component
public class StompConnectedEvent implements ApplicationListener<SessionConnectedEvent> {

    private static final Logger logger = LoggerFactory.getLogger(StompConnectedEvent.class);

    @Autowired
    private UserMessageController userMessageController;

    @Override
    public void onApplicationEvent(SessionConnectedEvent event) {
        logger.debug("Client connected.");
        userMessageController.getAllUsers();
    }
}
