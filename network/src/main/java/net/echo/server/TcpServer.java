package net.echo.server;

import net.echo.server.bootstrap.Settings;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.Pipeline;
import net.echo.server.pipeline.transmitters.Transmitter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    public boolean isRunning() {
        return running;
    }

    private void handleClientMessages(Channel channel, C context) {
        ByteBuffer buffer = ByteBuffer.allocate(1024 * 8);

        channel.getSocketChannel().read(buffer, buffer, new CompletionHandler<>() {
            @Override
            public void completed(Integer result, ByteBuffer buffer) {
                if (result == -1) {
                    channel.close();
                    return;
                }

                List<Transmitter<C>> transmitters = pipeline.getTransmitters();

                Object input = buffer;

                for (Transmitter<C> transmitter : transmitters) {
                    if (!(transmitter instanceof Transmitter.In<C, ?, ?> in) || input == null) continue;

                    try {
                        input = in.readBasic(context, input);
                    } catch (IOException e) {
                        in.handleException(context, e);
                    }
                }

                handleClientMessages(channel, context);
            }

            @Override
            public void failed(Throwable throwable, ByteBuffer attachment) {
                LOGGER.error(throwable);
            }
        });
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

                try {
                    input = out.writeBasic(connection, input);
                } catch (IOException e) {
                    out.handleException(connection, e);
                }
            }

        }
    }

    private record ConnectionHandler<T>(TcpServer<T> server)
            implements CompletionHandler<AsynchronousSocketChannel, Object> {

        @Override
        public void completed(AsynchronousSocketChannel result, Object attachment) {
            if (!server.running) return;

            Channel channel = new Channel(server, result);
            T context = server.createConnection(channel);

            server.connectionMap.put(channel, context);
            server.executor.submit(() -> server.handleClientMessages(channel, context));
            server.serverChannel.accept(null, this);
        }

        @Override
        public void failed(Throwable throwable, Object attachment) {
            LOGGER.error(throwable);
        }
    }
}
