package ru.otus.chart.core.utils;

import org.apache.kafka.common.serialization.Deserializer;
import ru.otus.chart.core.utils.exceptions.SerializerError;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.util.Map;

public class MessageDeserializer implements Deserializer {

    @Override
    public void configure(Map configs, boolean isKey) {
    }

    @Override
    public void close() {
    }

    @Override
    public Object deserialize(String topic, byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
            ObjectInputStream is = new ObjectInputStream(bis)) {
            return is.readObject();
        } catch (Exception e) {
            throw new SerializerError("DeSerialization error", e);
        }
    }
}
