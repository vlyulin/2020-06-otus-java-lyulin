package ru.otus.chart.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import ru.otus.chart.configurations.ChartKafkaConsumerConfig;
import ru.otus.chart.core.model.ModuleOperationsData;

@Controller
public class ChartController {
    private static final Logger logger = LoggerFactory.getLogger(ChartController.class);
    private final SimpMessagingTemplate template;
    private final ChartKafkaConsumerConfig chartKafkaConsumerConfig;

    public ChartController(SimpMessagingTemplate template, ChartKafkaConsumerConfig chartKafkaConsumerConfig) {
        this.template = template;
        this.chartKafkaConsumerConfig = chartKafkaConsumerConfig;
    }

    @MessageMapping("/askchartdata")
    public void getChartData() {
        logger.info("askchartdata()");
        ModuleOperationsData[] statistics = chartKafkaConsumerConfig.getModularStatistics();
        this.template.convertAndSend("/topic/chartdata", statistics);
    }
}
