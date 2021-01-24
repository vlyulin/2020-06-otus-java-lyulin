package ru.otus.messagesystem.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.MessageSystemImpl;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;

@Configuration
public class MessageSystemConfiguration {

    @Bean
    public MessageSystem getMessageSystem() {
	MessageSystem messageSystem = new MessageSystemImpl();
        messageSystem.start();
        return messageSystem;
    }

    @Bean
    public CallbackRegistry getCallbackRegistry() {
        return new CallbackRegistryImpl();
    }

}
