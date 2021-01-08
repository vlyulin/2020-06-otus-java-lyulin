package ru.otus.dbserver.messagesystem;

import ru.otus.common.core.messagetypes.OperationStatusMsgData;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.RequestHandler;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.netsystem.KafkaMsClientImpl;

import java.util.ArrayList;
import java.util.Optional;

public class RegisterClientRequestHandler implements RequestHandler<OperationStatusMsgData> {
    public static final String SUCCESS = "SUCCESS";
    public static final String ERROR = "ERROR";
    private MessageSystem messageSystem;
    public RegisterClientRequestHandler(MessageSystem messageSystem) {
        this.messageSystem = messageSystem;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        OperationStatusMsgData operationStatusMsgData;
        MsClient toFrontendMsClient = new KafkaMsClientImpl(msg.getFrom(), msg.getFrom()/*DATABASE_PRODUCER*/);
        messageSystem.addClient(toFrontendMsClient);
        operationStatusMsgData = new OperationStatusMsgData(SUCCESS, new ArrayList<>());
        return Optional.of(MessageBuilder.buildReplyMessage(msg, operationStatusMsgData, MessageType.OPERATION_STATUS));
    }
}
