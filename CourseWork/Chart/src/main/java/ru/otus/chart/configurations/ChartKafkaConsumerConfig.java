package ru.otus.chart.configurations;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import ru.otus.chart.core.model.ModuleOperationsData;
import ru.otus.chart.core.utils.MessageDeserializer;
import ru.otus.messagesystem.message.Message;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
@PropertySource("classpath:/kafka.properties")
public class ChartKafkaConsumerConfig {

    private static final Logger logger = LoggerFactory.getLogger(ChartKafkaConsumerConfig.class);
    private final String kafkaServer;
    private final String kafkaTopic;
    private final String kafkaGroupId;
    private final SimpMessagingTemplate template;

    private final HashMap<String, ModuleOperationsData> modularStatistics = new HashMap<>();

    public ChartKafkaConsumerConfig(
            @Value("${kafka.server}") String kafkaServer,
            @Value("${kafka.topic}") String kafkaTopic,
            @Value("${kafka.chart.group.id}") String kafkaGroupId,
            @Autowired SimpMessagingTemplate template
    ) {
        this.kafkaServer = kafkaServer;
        this.kafkaTopic = kafkaTopic;
        this.kafkaGroupId = kafkaGroupId;
        this.template = template;
    }

    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest"); // "latest"
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaServer);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, MessageDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, kafkaGroupId);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, true);
        return props;
    }

    public ConsumerFactory<String, Message> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    public ConcurrentMessageListenerContainer ChartKafkaListener(
            Consumer<Object> consumer
    ) {
        ContainerProperties containerProperties = new ContainerProperties(this.kafkaTopic);
        containerProperties.setMessageListener(
                (MessageListener<String, Object>) record -> {
                    Object msg = record.value();
                    consumer.accept(msg);
                });

        ConcurrentMessageListenerContainer container =
                new ConcurrentMessageListenerContainer<>(
                        consumerFactory(),
                        containerProperties
                );

        return container;
    }

    void processOperation(Message msg) {
        ModuleOperationsData module;
        if(modularStatistics.containsKey(msg.getFrom())){
            module = modularStatistics.get(msg.getFrom());
        } else {
            module = new ModuleOperationsData(msg.getFrom());
            modularStatistics.put(msg.getFrom(), module);
        }
        Map<String,Integer> operations = module.getChartData();
        String key = msg.getType().getName();
        if(operations.containsKey(key)) {
            operations.replace(key,operations.get(key) + 1);
        } else {
            operations.put(key,1);
        }
    }

    @Bean("ChartListener")
    public ConcurrentMessageListenerContainer getListener() {
        Consumer<Object> messageConsumer = msg -> {
            if(msg instanceof Message) {
                processOperation((Message)msg);
                Collection<ModuleOperationsData> collection = modularStatistics.values();
                ModuleOperationsData[] moduleOperationsDataArr = collection.toArray(new ModuleOperationsData[collection.size()]);
                this.template.convertAndSend("/topic/chartdata", moduleOperationsDataArr);
            }
        };

        ConcurrentMessageListenerContainer listener = ChartKafkaListener(messageConsumer);
        return listener;
    }

    public ModuleOperationsData[] getModularStatistics() {
//        Map<String, Integer> mapFront1 = Stream.of(new Object[][] {
//                { "FilteredUsersData", 8 },
//                { "SaveUser", 2 }
//        }).collect(Collectors.toMap(data -> (String) data[0], data -> (Integer) data[1]));
//        modularStatistics.put("Frontend1", new ModuleOperationsData("Frontend1", mapFront1));
        Collection<ModuleOperationsData> collection = modularStatistics.values();
        ModuleOperationsData[] moduleOperationsDataArr = collection.toArray(new ModuleOperationsData[collection.size()]);
        return moduleOperationsDataArr;
    }
}
