package ru.otus.frontend.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.frontend.messagesystem.FrontEndResponseHandler;
import ru.otus.frontend.messagesystem.FrontendService;
import ru.otus.frontend.messagesystem.FrontendServiceImpl;
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
public class FrontendMessageSystemConfiguration {

    public static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    @Bean("frontendMessageSystem")
    public MessageSystem getMessageSystem() {
        return new MessageSystemImpl();
    }

    @Bean("frontendCallbackRegistry")
    public CallbackRegistry getCallbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("frontendMsClient")
    public MsClient getFrontendMsClient(
            @Autowired @Qualifier("frontendMessageSystem") MessageSystem messageSystem,
            @Autowired @Qualifier("frontendCallbackRegistry") CallbackRegistry callbackRegistry
    ){
        FrontEndResponseHandler frontEndResponseHandler = new FrontEndResponseHandler(callbackRegistry);
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.GET_FILTERED_USERS, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.GET_USER, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.SAVE_USER, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.OPERATION_STATUS, frontEndResponseHandler);

        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }

    @Bean("FrontToDbMsClient")
    public MsClient getDbMsClient(
            @Value("${messageserver.host}") String messageSystemHost,
            @Value("${messageserver.port}") String messageSystemPort,
            @Autowired @Qualifier("frontendMessageSystem") MessageSystem messageSystem
    ) {
        // DB обработчики (на самом деле отправка по сети)
        MsClient dbMsClient = new NetClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystemHost, Integer.valueOf(messageSystemPort));
        messageSystem.addClient(dbMsClient);
        return dbMsClient;
    }

    @Bean
    FrontendService getFrontendService(@Qualifier("frontendMsClient") MsClient msClient) {
        return new FrontendServiceImpl(msClient, DATABASE_SERVICE_CLIENT_NAME);
    }

    @Bean("FrontEndListener")
    Listener getListener (
            @Qualifier("frontendMessageSystem") MessageSystem messageSystem,
            @Value("${FeListener.host}") String host,
            @Value("${FeListener.port}") Integer port
    ) {
        Listener listener = new ListenerImpl(messageSystem, host, port);
        listener.start();
        return listener;
    }
}
