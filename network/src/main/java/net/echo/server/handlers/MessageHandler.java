package net.echo.server.handlers;

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
    public void completed(Integer result, ByteBuffer attachment) {
        if (attachment == null) {
            throw new IllegalStateException("Attachment is null");
        }

        if (result == -1) {
            server.getCachedPipeline().getHandlers().forEach(handler -> handler.onChannelDisconnect(context));
            channel.close();
            return;
        }

        List<Transmitter<T>> transmitters = server.getCachedPipeline().getTransmitters();
        Object input = attachment;

        for (Transmitter<T> transmitter : transmitters) {
            if (transmitter instanceof Transmitter.In<T, ?, ?> in && input != null) {
                input = in.readObject(context, input);
            }
        }

        if (input != null) {
            for (InboundHandler<T, ?> handler : server.getCachedPipeline().getHandlers()) {
                handler.handleObject(context, input);
            }
        }

        System.out.println("Finished reading, clearing and reading again");

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        channel.getSocketChannel().read(buffer, buffer, this);
    }

    @Override
    public void failed(Throwable exc, ByteBuffer attachment) {
        LOGGER.error("Exception while reading", exc);
    }
}
