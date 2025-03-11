package net.echo.sparky.network.pipeline.inbound;

import net.echo.server.NetworkBuffer;
import net.echo.server.pipeline.transmitters.Transmitter.In;
import net.echo.sparky.network.player.PlayerConnection;

import java.nio.ByteBuffer;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class MessageSplitter implements In<PlayerConnection, ByteBuffer, ByteBuffer> {

    @Override
    public ByteBuffer read(PlayerConnection connection, ByteBuffer buffer) {
        NetworkBuffer networkBuffer = new NetworkBuffer(buffer);
        networkBuffer.mark();

        byte[] bytes = new byte[3];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = networkBuffer.readByte();

            if (bytes[i] >= 0) {
                NetworkBuffer packetBuffer = new NetworkBuffer(ByteBuffer.wrap(bytes));

                int length = packetBuffer.readVarInt();

                if (networkBuffer.readableBytes() >= length) {
                    return ByteBuffer.wrap(networkBuffer.readBytes(length));
                }

                networkBuffer.reset();
            }
        }

        buffer.reset();
        return null;
    }

    @Override
    public void handleException(PlayerConnection connection, Exception exception) {
        LOGGER.error(exception);
        connection.getChannel().close();
    }
}
