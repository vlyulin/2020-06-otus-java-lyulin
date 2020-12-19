package ru.otus.messagesystem.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

// TODO: Delete public
public class Serializers {
    private Serializers() {
    }

    // TODO: delete public
    public static byte[] serialize(Object data) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
             ObjectOutputStream os = new ObjectOutputStream(baos)) {
            os.writeObject(data);
            os.flush();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new SerializerError("Serialization error", e);
        }
    }

    // TODO: delete public
    public static Object deserialize(byte[] data) {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(data);
             ObjectInputStream is = new ObjectInputStream(bis)) {
            return is.readObject();
        } catch (Exception e) {
            throw new SerializerError("DeSerialization error", e);
        }
    }
}
