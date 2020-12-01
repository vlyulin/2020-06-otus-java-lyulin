package ru.otus.frontend.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.frontend.core.dao.UserDao;
import ru.otus.messagesystem.*;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

@Configuration
public class DatabaseMessageSystemConfiguration {

    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean("databaseMsClient")
    public MsClient getDatabaseMsClient(
            @Autowired MessageSystem messageSystem,
            @Autowired CallbackRegistry callbackRegistry,
            @Autowired UserDao userDao
    ){
        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.GET_FILTERED_USERS, new GetFilteredUserListDataRequestHandler(userDao));
        requestHandlerDatabaseStore.addHandler(MessageType.GET_USER, new GetUserRequestHandler(userDao));
        requestHandlerDatabaseStore.addHandler(MessageType.SAVE_USER, new SaveUserRequestHandler(userDao));
        requestHandlerDatabaseStore.addHandler(MessageType.DELETE_USER, new DeleteUserRequestHandler(userDao));

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }
}
