package ru.otus.frontend.netsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;

import java.util.Optional;

public class NetResponseHandler implements RequestHandler<ResultDataType> {

    private static final Logger logger = LoggerFactory.getLogger(NetResponseHandler.class);
    private NetClient netClient;

    public NetResponseHandler(NetClient netClient) {
        this.netClient = netClient;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        try {
            netClient.sendMessage(msg);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return Optional.empty();
    }
}
