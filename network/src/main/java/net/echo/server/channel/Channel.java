package net.echo.server.channel;

import net.echo.server.attributes.Attribute;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

public class Channel {

    private final AsynchronousSocketChannel socketChannel;
    private final Map<String, Attribute<?>> attributeMap;

    public Channel(AsynchronousSocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        this.attributeMap = new HashMap<>();
    }

    public AsynchronousSocketChannel getSocketChannel() {
        return socketChannel;
    }

    public boolean isOpen() {
        return socketChannel.isOpen();
    }

    public Future<Integer> write(ByteBuffer data) {
        return socketChannel.write(data);
    }

    public <T> void setAttribute(Attribute<T> attribute) {
        attributeMap.put(attribute.getIdentifier(), attribute);
    }

    public <T> Attribute<T> getAttribute(Attribute<T> attribute) {
        return (Attribute<T>) attributeMap.get(attribute.getIdentifier());
    }

    public void close() {
        try {
            socketChannel.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
