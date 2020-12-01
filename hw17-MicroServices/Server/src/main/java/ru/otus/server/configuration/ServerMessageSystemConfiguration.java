package ru.otus.server.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.netsystem.Listener;
import ru.otus.netsystem.ListenerImpl;
import ru.otus.netsystem.NetClientImpl;


@Configuration
@PropertySource("classpath:/application.properties")
public class ServerMessageSystemConfiguration {

    public static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";
    public static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";

    @Bean("serverMessageSystem")
    public MessageSystem getMessageSystem() {
        return new MessageSystemImpl();
    }

    @Bean("serverCallbackRegistry")
    public CallbackRegistry getCallbackRegistry() {
        return new CallbackRegistryImpl();
    }

    @Bean("feMsClient")
    public MsClient getFrontendMsClient(
            @Value("${frontend.host}") String messageSystemHost,
            @Value("${frontend.port}") String messageSystemPort,
            @Autowired @Qualifier("serverMessageSystem") MessageSystem messageSystem
    ) {
        // DB обработчики (на самом деле отправка по сети)
        MsClient frontendMsClient = new NetClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystemHost, Integer.valueOf(messageSystemPort));
        messageSystem.addClient(frontendMsClient);
        return frontendMsClient;
    }

    @Bean("dbMsClient")
    public MsClient getDbMsClient(
            @Value("${db.host}") String messageSystemHost,
            @Value("${db.port}") String messageSystemPort,
            @Autowired @Qualifier("serverMessageSystem") MessageSystem messageSystem
    ) {
        // DB обработчики (на самом деле отправка по сети)
        MsClient dbMsClient = new NetClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystemHost, Integer.valueOf(messageSystemPort));
        messageSystem.addClient(dbMsClient);
        return dbMsClient;
    }

    @Bean("ServerListener")
    Listener getListener(
            @Qualifier("serverMessageSystem") MessageSystem messageSystem,
            @Value("${serverListener.host}") String host,
            @Value("${serverListener.port}") Integer port
    ) {
        Listener listener = new ListenerImpl(messageSystem, host, port);
        return listener;
    }
}
