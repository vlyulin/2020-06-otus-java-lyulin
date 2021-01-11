package ru.otus.netsystem;

import org.springframework.kafka.core.KafkaTemplate;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class KafkaMsClientImpl implements MsClient {

    private final String msClientName;
    private final String kafkaProducerId;
    private final String topic;
    private final KafkaTemplate<Long, Message> kafkaTemplate;

    public KafkaMsClientImpl(String msClientName, String kafkaProducerId) {
        this.msClientName = msClientName;
        this.kafkaProducerId = kafkaProducerId;

        Properties prop = new Properties();
        try (final InputStream stream =
                     Thread.currentThread().getContextClassLoader()
                             .getResourceAsStream("kafka.properties")) {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        prop.putIfAbsent("kafka.kafkaProducerId",kafkaProducerId);
        this.topic = prop.getProperty("kafka.topic");
        this.kafkaTemplate = KafkaProducerConfig.kafkaTemplate(prop);
    }

    @Override
    public boolean sendMessage(Message msg) {
        kafkaTemplate.send(topic, msg);
        return true;
    }

    @Override
    public void handle(Message msg) {
        if(!sendMessage(msg)) {
            throw new RuntimeException("Error send msg: " + msg.toString());
        }
    }

    @Override
    public String getName() {
        return msClientName;
    }

    @Override
    public <T extends ResultDataType> Message produceMessage(String to, T data, MessageType msgType, MessageCallback<T> callback) {
        Message message = MessageBuilder.buildMessage(msClientName, to, null, data, msgType);
        return message;
    }

    @Override
    public <X, T extends ResultDataType> Message produceMessage(String to, X data, MessageType msgType, MessageCallback<T> callback) {
        Message message = MessageBuilder.buildMessage(msClientName, to, null, data, msgType);
        return message;
    }

}
