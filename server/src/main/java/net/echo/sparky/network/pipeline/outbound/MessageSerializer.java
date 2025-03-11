package net.echo.sparky.network.pipeline.outbound;

import net.echo.server.NetworkBuffer;
import net.echo.server.pipeline.transmitters.Transmitter;
import net.echo.sparky.network.player.PlayerConnection;

import java.io.IOException;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class MessageSerializer implements Transmitter.Out<PlayerConnection, NetworkBuffer, Void> {

    @Override
    public Void write(PlayerConnection connection, NetworkBuffer buffer) throws IOException {
        int length = buffer.readableBytes();

        NetworkBuffer outBuffer = new NetworkBuffer();

        outBuffer.writeVarInt(length);
        outBuffer.writeBytes(buffer.getBuffer().array(), outBuffer.readableBytes(), length);

        return null;
    }

    @Override
    public void handleException(PlayerConnection connection, Exception exception) {
        LOGGER.error(exception);
        connection.getChannel().close();
    }
}
