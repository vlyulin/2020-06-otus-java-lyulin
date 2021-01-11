package ru.otus.dbserver.configurations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.dbserver.core.dao.UserDao;
import ru.otus.dbserver.messagesystem.*;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.HandlersStoreImpl;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.netsystem.KafkaListenerImpl;
import ru.otus.netsystem.KafkaMsClientImpl;
import ru.otus.netsystem.Listener;

import java.util.function.Consumer;

@Configuration
@ComponentScan(basePackages = {"ru.otus.dbserver", "ru.otus.netsystem"})
@PropertySource("classpath:/application.properties")
public class DatabaseMessageSystemConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseMessageSystemConfiguration.class);
    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    public static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    public static final String DATABASE_PRODUCER = "DatabaseProducer";
    private MessageSystem messageSystem;

    @Bean("dbMessageSystem")
    public MessageSystem getMessageSystem() {
        messageSystem = new MessageSystemImpl(DATABASE_SERVICE_CLIENT_NAME);
        return messageSystem;
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
        requestHandlerDatabaseStore.addHandler(MessageType.REGISTER_CLIENT, new RegisterClientRequestHandler(messageSystem));

        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);
        return databaseMsClient;
    }

    @Bean("DbToFrontMsClient")
    public MsClient getDbMsClient(
            @Autowired @Qualifier("dbMessageSystem") MessageSystem messageSystem
    ) {
        // отправка на фронт
        MsClient toFrontendMsClient = new KafkaMsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, DATABASE_PRODUCER);
        messageSystem.addClient(toFrontendMsClient);
        return toFrontendMsClient;
    }

    @Bean("dbServerListener")
    Listener getListener(
            @Qualifier("dbMessageSystem") MessageSystem messageSystem
    ) {
        Consumer<Object> messageConsumer = msg -> {
            if(msg instanceof Message) {
                Message message = (Message) msg;
                if (message.getTo().equals(messageSystem.getName())) {
                    messageSystem.newMessage(message);
                }
            }
        };
        Listener listener = new KafkaListenerImpl(DATABASE_SERVICE_CLIENT_NAME, messageConsumer);
        listener.start();
        return listener;
    }
}
