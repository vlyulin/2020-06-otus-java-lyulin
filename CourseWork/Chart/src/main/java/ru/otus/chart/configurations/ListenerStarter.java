package ru.otus.chart.configurations;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.stereotype.Component;

// Примечание: вызывается после инициализации всех бинов
@Component
public class ListenerStarter implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext ctx = event.getApplicationContext();
        ConcurrentMessageListenerContainer chartListener = (ConcurrentMessageListenerContainer)ctx.getBean("ChartListener");
        chartListener.start();
    }
}
