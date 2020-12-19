package ru.otus.dbserver.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.dbserver.messagesystem.DeleteUserRequestHandler;
import ru.otus.dbserver.messagesystem.GetFilteredUserListDataRequestHandler;
import ru.otus.dbserver.messagesystem.GetUserRequestHandler;
import ru.otus.dbserver.messagesystem.SaveUserRequestHandler;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.netsystem.Listener;
import ru.otus.netsystem.ListenerImpl;
import ru.otus.netsystem.NetClientImpl;

@Configuration
@ComponentScan(basePackages = "ru.otus")
@PropertySource("classpath:/application.properties")
public class DatabaseMessageSystemConfiguration {

    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    public static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";

    @Bean("dbMessageSystem")
    public MessageSystem getMessageSystem() {
//        return new MessageSystemImpl("dbMessageSystem");
        return new MessageSystemImpl();
    }

    @Bean("dbCallbackRegistry")
    public CallbackRegistry getCallbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("databaseMsClient")
    public MsClient getDatabaseMsClient(
            @Autowired @Qualifier("dbMessageSystem") MessageSystem messageSystem,
            @Autowired @Qualifier("dbCallbackRegistry") CallbackRegistry callbackRegistry,
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

    @Bean("DbToFrontMsClient")
    public MsClient getDbToFrontendMsClient(
            @Value("${messageserver.host}") String messageSystemHost,
            @Value("${messageserver.port}") String messageSystemPort,
            @Autowired @Qualifier("dbMessageSystem") MessageSystem messageSystem
    ) {
        // DB обработчики (на самом деле отправка по сети)
        MsClient toFrontendMsClient = new NetClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystemHost, Integer.valueOf(messageSystemPort));
        messageSystem.addClient(toFrontendMsClient);
        return toFrontendMsClient;
    }

    @Bean("DbServerListener")
    Listener getListener(
            @Qualifier("dbMessageSystem") MessageSystem messageSystem,
            @Value("${dbListener.host}") String host,
            @Value("${dbListener.port}") Integer port
    ) {
        Listener listener = new ListenerImpl(messageSystem, host, port);
        return listener;
    }
}
