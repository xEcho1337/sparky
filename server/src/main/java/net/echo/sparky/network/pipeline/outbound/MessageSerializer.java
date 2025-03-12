package net.echo.sparky.network.pipeline.outbound;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.server.pipeline.transmitters.Transmitter;
import net.echo.sparky.network.player.PlayerConnection;

import java.io.IOException;
import java.nio.ByteBuffer;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class MessageSerializer implements Transmitter.Out<PlayerConnection, NetworkBuffer, ByteBuffer> {

    @Override
    public ByteBuffer write(PlayerConnection connection, NetworkBuffer buffer) throws IOException {
        buffer.getBuffer().flip();
        int length = buffer.remaining();

        NetworkBuffer outBuffer = new NetworkBuffer(ByteBuffer.allocate(length + 5));
        outBuffer.writeVarInt(length);

        byte[] data = buffer.getBuffer().array();
        outBuffer.writeBytes(data, 0, length);

        ByteBuffer result = outBuffer.getBuffer();
        result.flip();

        return result;
    }

    @Override
    public void handleException(PlayerConnection connection, Exception exception) {
        LOGGER.error("Exception serializing!", exception);
        connection.getChannel().close();
    }
}
