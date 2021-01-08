<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4" />

# Установка kafka

1. Первоначальная установка и запуск kafka
   В директории, где расположен файл docker-compose.yml выполнить команду
   docker-compose up -d

2. Зайти в контейнер
   docker exec -it kafka_kafka_1 bash

3. Создать топик
   /opt/kafka/bin/kafka-topics.sh --create --zookeeper zookeeper:2181 --replication-factor 1 --partitions 1 --topic servicebus

# Если что-то пошло не так
## проверить запущена ли kafka
docker-compose ps

## delete topic
Зайти в контейнер
docker exec -it kafka_kafka_1 bash
Удалить топик
/opt/kafka/bin/kafka-topics.sh --zookeeper zookeeper:2181 --delete --topic servicebus

