package net.echo.server.handlers;

import net.echo.server.TcpServer;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.handler.InboundHandler;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import static net.echo.server.TcpServer.LOGGER;

public record ConnectionHandler<T>(TcpServer<T> server)
        implements CompletionHandler<AsynchronousSocketChannel, Object> {

    @Override
    public void completed(AsynchronousSocketChannel result, Object attachment) {
        AsynchronousServerSocketChannel serverChannel = server.getServerChannel();

        if (server.isRunning() && serverChannel.isOpen()) {
            serverChannel.accept(null, this);
        }

        Channel channel = new Channel(server, result);
        T context = server.createConnection(channel);

        server.getCachedPipeline().getHandlers().forEach(handler -> handler.onChannelConnect(context));
        server.getConnectionMap().put(channel, context);

        ByteBuffer buffer = ByteBuffer.allocate(256);
        result.read(buffer, buffer, new MessageHandler<>(server, context, channel));
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        LOGGER.error(exc);
    }
}
