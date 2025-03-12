package net.echo.server.handlers;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.server.TcpServer;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.handler.InboundHandler;
import net.echo.server.pipeline.transmitters.Transmitter;

import java.nio.ByteBuffer;
import java.nio.channels.CompletionHandler;
import java.util.List;

import static net.echo.server.TcpServer.LOGGER;

public record MessageHandler<T>(TcpServer<T> server, T context, Channel channel)
        implements CompletionHandler<Integer, ByteBuffer> {

    @Override
    public void completed(Integer result, ByteBuffer buffer) {
        if (buffer == null) {
            throw new IllegalStateException("Buffer is null");
        }

        if (result == -1 || !channel.isOpen()) {
            server.getCachedPipeline().getHandlers().forEach(handler -> handler.onChannelDisconnect(context));
            channel.close();

            return;
        }

        buffer.flip();

        while (buffer.remaining() > 0) {
            NetworkBuffer networkBuffer = new NetworkBuffer(buffer);
            networkBuffer.mark();

            int packetLength = networkBuffer.readVarInt();

            if (buffer.remaining() >= packetLength) {
                byte[] bytes = networkBuffer.readBytes(packetLength);

                List<Transmitter<T>> transmitters = server.getCachedPipeline().getTransmitters();
                Object input = new NetworkBuffer(ByteBuffer.wrap(bytes));

                for (Transmitter<T> transmitter : transmitters) {
                    if (input == null) continue;

                    if (!(transmitter instanceof Transmitter.In<T, ?, ?> in)) continue;

                    input = in.readObject(context, input);
                }

                if (input != null) {
                    for (InboundHandler<T, ?> handler : server.getCachedPipeline().getHandlers()) {
                        handler.handleObject(context, input);
                    }
                }
            } else {
                networkBuffer.reset();
                break;
            }
        }

        ByteBuffer next = buffer.remaining() == 0 ? ByteBuffer.allocate(256) : buffer;

        if (!channel.isOpen()) {
            LOGGER.warn("Tried to read but the channel is closed!");
            return;
        }

        channel.getSocketChannel().read(next, next, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer buffer) {
        LOGGER.error("Exception while reading!", exc);
    }
}
