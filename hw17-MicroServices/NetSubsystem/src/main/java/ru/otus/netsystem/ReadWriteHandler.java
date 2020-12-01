package ru.otus.netsystem;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;

public class ReadWriteHandler implements CompletionHandler<Integer, Object> {

    @Override
    public void completed(Integer result, Object clientChannel) {
        final ByteBuffer buf = ByteBuffer.allocate(2048);
    }

    @Override
    public void failed(Throwable exc, Object attachment) {

    }
}
