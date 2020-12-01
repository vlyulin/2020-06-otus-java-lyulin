package ru.otus.netsystem;

import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messagesystem.message.Serializers;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Objects;
import java.util.concurrent.Future;

public class NetClientImpl implements MsClient {

    private final String name;
    private final String host;
    private final Integer port;
    private final InetSocketAddress hostAddress;

    public NetClientImpl(String name, String host, Integer port) {
        this.name = name;
        this.host = host;
        this.port = port;
        this.hostAddress = new InetSocketAddress(host, port);
    }

    @Override
    public boolean sendMessage(Message msg) {
        try {
            AsynchronousSocketChannel asyncSocketChannel = AsynchronousSocketChannel.open();
            Future<Void> future = asyncSocketChannel.connect(hostAddress);
            future.get();
            byte[] bytes = Serializers.serialize(msg);
            ByteBuffer buf = ByteBuffer.wrap(bytes);
            Future<Integer> writeResult = asyncSocketChannel.write(buf);
            writeResult.get();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

//        asyncSocketChannel.connect(
//                new InetSocketAddress(host, port),
//                asyncSocketChannel,
//                new CompletionHandler<Void, AsynchronousSocketChannel>() {
//                    @Override
//                    public void completed(Void result, AsynchronousSocketChannel attachment) {
//                        //write an message to server side
//                        startWrite( asyncSocketChannel, message );
//                    }
//
//                    @Override
//                    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
//
//                    }
//                }
//        );

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
        return name;
    }

    @Override
    public <T extends ResultDataType> Message produceMessage(String to, T data, MessageType msgType, MessageCallback<T> callback) {
//        throw new RuntimeException("Operation produceMessage is not allowed.");
//        return null;
        Message message = MessageBuilder.buildMessage(name, to, null, data, msgType);
//        callbackRegistry.put(message.getCallbackId(), callback);
        return message;
    }

    @Override
    public <X, T extends ResultDataType> Message produceMessage(String to, X data, MessageType msgType, MessageCallback<T> callback) {
//        throw new RuntimeException("Operation produceMessage is not allowed.");
//        return null;
        Message message = MessageBuilder.buildMessage(name, to, null, data, msgType);
        return message;
    }

    //    @Override
//    public void sendMessage(final Message message) throws IOException, ExecutionException, InterruptedException {
//        AsynchronousSocketChannel asyncSocketChannel = AsynchronousSocketChannel.open();
////        InetSocketAddress hostAddress = new InetSocketAddress(host, port);
//        Future<Void> future = asyncSocketChannel.connect(hostAddress);
//        future.get();
//        byte[] bytes = Serializers.serialize(message);
//        ByteBuffer buf = ByteBuffer.wrap(bytes);
//        Future<Integer> writeResult = asyncSocketChannel.write(buf);
//        writeResult.get();
////        asyncSocketChannel.connect(
////                new InetSocketAddress(host, port),
////                asyncSocketChannel,
////                new CompletionHandler<Void, AsynchronousSocketChannel>() {
////                    @Override
////                    public void completed(Void result, AsynchronousSocketChannel attachment) {
////                        //write an message to server side
////                        startWrite( asyncSocketChannel, message );
////                    }
////
////                    @Override
////                    public void failed(Throwable exc, AsynchronousSocketChannel attachment) {
////
////                    }
////                }
////        );
//    }

    private void startWrite( final AsynchronousSocketChannel sockChannel, final Message message ) {
        byte[] bytes = Serializers.serialize(message);
        ByteBuffer buf = ByteBuffer.wrap(bytes);

//        buf.flip();
//        messageWritten.getAndIncrement();
        sockChannel.write(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel ) {
                //after message written
                //NOTHING TO DO
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println( "Fail to write the message to server");
            }
        });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
