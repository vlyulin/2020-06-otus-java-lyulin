package ru.otus.netsystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageHelper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

public class ListenerImpl implements Listener {

    private static final Logger logger = LoggerFactory.getLogger(ListenerImpl.class);

    private MessageSystem messageSystem;
    private AsynchronousServerSocketChannel serverSocketChannel;
    private String host;
    private Integer port;

    public ListenerImpl(
            MessageSystem messageSystem,
            String host,
            Integer port
    ) {
        this.messageSystem = messageSystem;
        this.host = host;
        this.port = port;
    }

    @Override
    public void start() {
        //start to accept the connection from client
        logger.info("{}:{}", host, port);
        try {
            serverSocketChannel = AsynchronousServerSocketChannel.open();
            InetSocketAddress hostAddress = new InetSocketAddress(host, port);
            serverSocketChannel.bind(hostAddress);
            serverSocketChannel.accept(serverSocketChannel, getCompletionHandler());
        } catch (IOException e) {
            throw new RuntimeException("Listener error: " + e.getMessage());
        }
    }

    private CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>
    getCompletionHandler() {
        return new CompletionHandler<AsynchronousSocketChannel, AsynchronousServerSocketChannel>() {

            @Override
            public void completed(AsynchronousSocketChannel sockChannel, AsynchronousServerSocketChannel serverSock) {
                if (serverSock.isOpen()) {
                    // a connection is accepted, start to accept next connection
                    serverSock.accept(serverSock, this);
                }
                System.out.println("a connection is accepted, start to accept next connection");
                //start to read message from the client
                if ((sockChannel != null) && (sockChannel.isOpen())) {
                    startReadConnection(sockChannel);
                }
            }

            @Override
            public void failed(Throwable exc, AsynchronousServerSocketChannel attachment) {
                System.out.println("fail to accept a connection");
            }
        };
    }

    private void startReadConnection(AsynchronousSocketChannel sockChannel) {
        final ByteBuffer buf = ByteBuffer.allocate(20480);

        //read message from client
        sockChannel.read(buf, sockChannel, new CompletionHandler<Integer, AsynchronousSocketChannel>() {

            /**
             * some message is read from client, this callback will be called
             */
            @Override
            public void completed(Integer result, AsynchronousSocketChannel channel) {
                logger.info("MessageSystem: read completion");
                Message msg = MessageHelper.deSerializeMessage(buf.array());
                messageSystem.newMessage(msg);

                //start to read next message again
                startReadConnection(channel);
            }

            @Override
            public void failed(Throwable exc, AsynchronousSocketChannel channel) {
                System.out.println("fail to read message from client");
            }
        });
    }
}
