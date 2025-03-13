package net.echo.server;

import net.echo.server.channel.Channel;
import net.echo.server.handlers.ConnectionHandler;
import net.echo.server.handlers.FlushHandler;
import net.echo.server.pipeline.Pipeline;
import net.echo.server.pipeline.transmitters.Transmitter;
import net.echo.server.settings.Settings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public abstract class TcpServer<C> {

    public static Logger LOGGER = LogManager.getLogger(TcpServer.class);

    protected final Map<Channel, C> connectionMap;
    protected final ExecutorService executor;
    protected final Pipeline<C> pipeline;
    protected final Settings settings;

    private AsynchronousServerSocketChannel serverChannel;
    private volatile boolean running;

    public TcpServer(Settings settings) {
        this.connectionMap = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(settings.threads());
        this.pipeline = getPipeline();
        this.settings = settings;
    }

    public Map<Channel, C> getConnectionMap() {
        return connectionMap;
    }

    public ExecutorService getExecutor() {
        return executor;
    }

    public AsynchronousServerSocketChannel getServerChannel() {
        return serverChannel;
    }

    public Pipeline<C> getCachedPipeline() {
        return pipeline;
    }

    public Settings getSettings() {
        return settings;
    }

    public boolean isRunning() {
        return running;
    }

    public abstract Pipeline<C> getPipeline();

    public abstract C createConnection(Channel channel);

    public void start(int port) {
        this.running = true;

        try {
            this.serverChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));
            this.serverChannel.accept(null, new ConnectionHandler<>(this));
        } catch (IOException e) {
            LOGGER.error(e);
        }
    }

    public void stop() {
        this.running = false;
        this.executor.shutdown();
    }

    private ByteBuffer transmitPacket(C connection, Object packet) {
        List<Transmitter<C>> transmitters = pipeline.getTransmitters();
        Object input = packet;

        for (Transmitter<C> transmitter : transmitters) {
            if (input == null) continue;

            if (!(transmitter instanceof Transmitter.Out<C, ?, ?> out)) continue;

            input = out.writeObject(connection, input);
        }

        if (!(input instanceof ByteBuffer byteBuffer)) {
            LOGGER.error("Closing pipeline doesn't return a ByteBuffer instance!");
            return null;
        }

        return byteBuffer;
    }

    public void flush(Channel channel, Object packet) {
        C connection = connectionMap.get(channel);

        if (connection == null) {
            throw new NullPointerException("Connection not stored");
        }

        ByteBuffer buffer = transmitPacket(connection, packet);

        channel.getSocketChannel().write(buffer, null, new FlushHandler(LOGGER));
    }

    public void flush(Channel channel, Collection<Object> writeQueue) {
        if (writeQueue.isEmpty()) return;

        C connection = connectionMap.get(channel);

        if (connection == null) {
            throw new NullPointerException("Connection not stored");
        }

        List<ByteBuffer> buffers = new ArrayList<>();

        for (Object packet : writeQueue) {
            buffers.add(transmitPacket(connection, packet));
        }

        // Optimization: merge all packets in a single byte buffer
        ByteBuffer combinedBuffer = ByteBuffer.allocate(estimateTotalSize(buffers));
        FlushHandler handler = new FlushHandler(LOGGER);

        System.out.println("Size: " + combinedBuffer.remaining());

        for (ByteBuffer byteBuffer : buffers) {
            if (combinedBuffer.remaining() < byteBuffer.remaining()) {
                combinedBuffer.flip();
                executor.submit(() -> channel.getSocketChannel().write(combinedBuffer, channel, handler));
                combinedBuffer.clear();
            }

            combinedBuffer.put(byteBuffer);
        }

        if (combinedBuffer.position() > 0) {
            combinedBuffer.flip();
            executor.submit(() -> channel.getSocketChannel().write(combinedBuffer, channel, handler));
        }

        writeQueue.clear();
    }

    private int estimateTotalSize(List<ByteBuffer> buffers) {
        int totalSize = 0;

        for (ByteBuffer byteBuffer : buffers) {
            totalSize += byteBuffer.remaining();
        }

        return totalSize > 0 ? totalSize : 1024;
    }
}
