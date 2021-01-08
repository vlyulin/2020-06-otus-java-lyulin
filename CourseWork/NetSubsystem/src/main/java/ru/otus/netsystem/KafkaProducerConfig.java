package ru.otus.netsystem;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import ru.otus.messagesystem.message.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class KafkaProducerConfig {

    public static Map<String, Object> producerConfigs(Properties prop) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, prop.getProperty("kafka.server"));
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, MessageSerializer.class); // JsonSerializer.class);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, prop.getProperty("kafka.kafkaProducerId"));
        return props;
    }

    public static ProducerFactory<Long, Message> producerFactory(Properties prop) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(prop));
    }

    public static KafkaTemplate<Long, Message> kafkaTemplate(Properties prop) {
        KafkaTemplate<Long, Message> template = new KafkaTemplate<>(producerFactory(prop));
        return template;
    }
}
