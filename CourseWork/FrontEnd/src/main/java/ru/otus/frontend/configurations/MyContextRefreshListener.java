package ru.otus.frontend.configurations;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

import static ru.otus.frontend.configurations.FrontendMessageSystemConfiguration.DATABASE_SERVICE_CLIENT_NAME;

// Примечание: вызывается после инициализации всех бинов
@Component
public class MyContextRefreshListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        MsClient frontendMsClient = (MsClient) ctx.getBean("frontendMsClient");

        if(frontendMsClient != null) {
            // Регистрация клиента в базе
            Message outMsg = frontendMsClient.produceMessage(
                    DATABASE_SERVICE_CLIENT_NAME,
                    null,
                    MessageType.REGISTER_CLIENT,
                    operationStatusMsgData -> {
                        operationStatusMsgData.toString();
                    });
            frontendMsClient.sendMessage(outMsg);
            // Окончание регистрации клиента в базе
        }
    }
}
