package ru.otus.frontend.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.*;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.MessageType;

@Configuration
public class FrontendMessageSystemConfiguration {

    public static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";

    @Bean("frontendMsClient")
    public MsClient getFrontendMsClient(
            @Autowired MessageSystem messageSystem,
            @Autowired CallbackRegistry callbackRegistry
    ){
        FrontEndResponseHandler frontEndResponseHandler = new FrontEndResponseHandler(callbackRegistry);
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.GET_FILTERED_USERS, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.GET_USER, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.SAVE_USER, frontEndResponseHandler); // TODO: под вопросом
        requestHandlerFrontendStore.addHandler(MessageType.OPERATION_STATUS, frontEndResponseHandler);

        MsClient frontendMsClient = new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }

    @Bean
    FrontendService getFrontendService(@Qualifier("frontendMsClient") MsClient msClient) {
        return new FrontendServiceImpl(msClient, DatabaseMessageSystemConfiguration.DATABASE_SERVICE_CLIENT_NAME);
    }
}
