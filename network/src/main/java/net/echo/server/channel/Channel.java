package net.echo.server.channel;

import net.echo.server.TcpServer;
import net.echo.server.attributes.Attribute;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("all")
public class Channel {

    private final TcpServer<?> server;
    private final AsynchronousSocketChannel socketChannel;
    private final Map<String, Attribute<?>> attributeMap;
    private final List<Object> writeQueue;

    public Channel(TcpServer<?> server, AsynchronousSocketChannel socketChannel) {
        this.server = server;
        this.socketChannel = socketChannel;
        this.attributeMap = new HashMap<>();
        this.writeQueue = new ArrayList<>();
    }

    public AsynchronousSocketChannel getSocketChannel() {
        return socketChannel;
    }

    public List<Object> getWriteQueue() {
        return writeQueue;
    }

    public boolean isOpen() {
        return socketChannel.isOpen();
    }

    public void flushQueue() {
        server.flush(this, writeQueue);
    }

    public void write(Object data) {
        writeQueue.add(data);
    }

    public void writeAndFlush(Object data) {
        write(data);
        flushQueue();
    }

    public <T> void setAttribute(Attribute<T> attribute, T value) {
        Attribute<T> copy = attributeMap.containsKey(attribute.getIdentifier())
                ? getAttribute(attribute)
                : attribute.copy();

        copy.setValue(value);
        attributeMap.put(attribute.getIdentifier(), copy);
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

    public SocketAddress remoteAddress() {
        try {
            return socketChannel.getRemoteAddress();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
