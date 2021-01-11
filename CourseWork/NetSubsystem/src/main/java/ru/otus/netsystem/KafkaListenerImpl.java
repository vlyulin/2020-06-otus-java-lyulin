package ru.otus.netsystem;

import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.function.Consumer;

public class KafkaListenerImpl implements Listener {

    ConcurrentMessageListenerContainer container;

    public KafkaListenerImpl(String groupId, Consumer<Object> consumer) {

        Properties prop = new Properties();
        try (final InputStream stream =
                     Thread.currentThread().getContextClassLoader()
                             .getResourceAsStream("kafka.properties")) {
            prop.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ContainerProperties containerProperties = new ContainerProperties(prop.getProperty("kafka.topic"));
        containerProperties.setMessageListener(
                (MessageListener<String, Object>) record -> {
                    Object msg = record.value();
                    consumer.accept(msg);
                });

        KafkaConsumerConfig kafkaConsumerConfig = new KafkaConsumerConfig(
                prop.getProperty("kafka.server"),
                groupId);

        container =
                new ConcurrentMessageListenerContainer<>(
                        kafkaConsumerConfig.consumerFactory(),
                        containerProperties
                );
    }

    @Override
    public void start() {
        container.start();
    }
}
