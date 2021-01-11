package ru.otus.frontend.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.netsystem.KafkaListenerImpl;
import ru.otus.netsystem.KafkaMsClientImpl;
import ru.otus.netsystem.Listener;

import java.util.function.Consumer;

@Configuration
@ComponentScan(basePackages = {"ru.otus.frontend","ru.otus.netsystem"})
@PropertySource("classpath:/application.properties")
public class FrontendMessageSystemConfiguration {

    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    public static final String FRONTEND_PRODUCER = "FrontendProducer";
    private MessageSystem messageSystem;

    @Bean("frontendMessageSystem")
    public MessageSystem getMessageSystem(
            @Value("${frontend.service.client.name:FRONTEND_SERVICE_CLIENT_NAME}") String frontendServiceClientName
    ) {
        messageSystem = new MessageSystemImpl(frontendServiceClientName); // FRONTEND_SERVICE_CLIENT_NAME);
        return messageSystem;
    }

    @Bean("frontendCallbackRegistry")
    public CallbackRegistry getCallbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("frontendMsClient")
    public MsClient getFrontendMsClient(
            @Value("${frontend.service.client.name:FRONTEND_SERVICE_CLIENT_NAME}") String frontendServiceClientName,
            @Autowired @Qualifier("frontendMessageSystem") MessageSystem messageSystem,
            @Autowired @Qualifier("frontendCallbackRegistry") CallbackRegistry callbackRegistry
    ) {
        FrontEndResponseHandler frontEndResponseHandler = new FrontEndResponseHandler(callbackRegistry);
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.GET_FILTERED_USERS, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.GET_USER, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.SAVE_USER, frontEndResponseHandler);
        requestHandlerFrontendStore.addHandler(MessageType.OPERATION_STATUS, frontEndResponseHandler);

        MsClient frontendMsClient = new MsClientImpl(frontendServiceClientName, // FRONTEND_SERVICE_CLIENT_NAME,
                messageSystem, requestHandlerFrontendStore, callbackRegistry);
        messageSystem.addClient(frontendMsClient);

        return frontendMsClient;
    }

    // Для обработки сообщений из базы
    @Bean("FrontToDbMsClient")
    public MsClient getDbMsClient(
            @Autowired @Qualifier("frontendMessageSystem") MessageSystem messageSystem
    ) {
        MsClient msClient = new KafkaMsClientImpl(DATABASE_SERVICE_CLIENT_NAME, FRONTEND_PRODUCER);
        messageSystem.addClient(msClient);
        return msClient;
    }

    @Bean
    FrontendService getFrontendService(@Qualifier("frontendMsClient") MsClient msClient) {
        return new FrontendServiceImpl(msClient, DATABASE_SERVICE_CLIENT_NAME);
    }

    @Bean("dbServerListener")
    Listener getListener(
            @Value("${frontend.service.client.name:FRONTEND_SERVICE_CLIENT_NAME}") String frontendServiceClientName,
            @Qualifier("frontendMessageSystem") MessageSystem messageSystem
    ) {
        Consumer<Object> messageConsumer = msg -> {
            if(msg instanceof Message) {
                Message message = (Message) msg;
                if (message.getTo().equals(messageSystem.getName())) {
                    messageSystem.newMessage(message);
                }
            }
        };
        Listener listener = new KafkaListenerImpl(frontendServiceClientName, messageConsumer);
        listener.start();
        return listener;
    }
}
