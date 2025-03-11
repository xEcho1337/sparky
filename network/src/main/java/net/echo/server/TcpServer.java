package net.echo.server;

import net.echo.server.bootstrap.Settings;
import net.echo.server.channel.Channel;
import net.echo.server.handlers.ConnectionHandler;
import net.echo.server.handlers.MessageHandler;
import net.echo.server.pipeline.Pipeline;
import net.echo.server.pipeline.transmitters.Transmitter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public abstract class TcpServer<C> {

    public static Logger LOGGER = LogManager.getLogger(TcpServer.class);

    private final Map<Channel, C> connectionMap;
    private final ExecutorService executor;
    private final Pipeline<C> pipeline;

    private AsynchronousServerSocketChannel serverChannel;
    private volatile boolean running;

    public TcpServer(Settings settings) {
        this.connectionMap = new HashMap<>();
        this.executor = Executors.newFixedThreadPool(settings.receiveThreads());
        this.pipeline = getPipeline();
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

    public void handleClientMessages(Channel channel, C context) {
        System.out.println("Waiting for a message");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();

        channel.getSocketChannel().read(buffer, buffer, new MessageHandler<>(this, context, channel));
    }

    public void flush(Channel channel, List<Object> writeQueue) {
        C connection = connectionMap.get(channel);

        if (connection == null) {
            throw new NullPointerException("Connection not stored");
        }

        for (Object object : writeQueue) {
            List<Transmitter<C>> transmitters = pipeline.getTransmitters();

            Object input = object;

            for (Transmitter<C> transmitter : transmitters) {
                if (!(transmitter instanceof Transmitter.Out<C, ?, ?> out)) continue;

                input = out.writeObject(connection, input);
            }
        }
    }

    public void forEachTransmitter(Consumer<Transmitter<C>> consumer) {
        for (Transmitter<C> transmitter : pipeline.getTransmitters()) {
            consumer.accept(transmitter);
        }
    }
}
