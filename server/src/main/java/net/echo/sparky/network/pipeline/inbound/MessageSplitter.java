package net.echo.sparky.network.pipeline.inbound;

import net.echo.server.NetworkBuffer;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.transmitters.Transmitter.In;
import net.echo.sparky.network.player.PlayerConnection;

import java.nio.ByteBuffer;
import java.util.Arrays;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class MessageSplitter implements In<PlayerConnection, ByteBuffer, ByteBuffer> {

    @Override
    public ByteBuffer read(PlayerConnection connection, ByteBuffer buffer) {
        NetworkBuffer networkBuffer = new NetworkBuffer(buffer);
        int readableBytes = networkBuffer.readableBytes(1024);

        System.out.println("Starting: " + readableBytes);
        if (readableBytes == 0) return null;

        networkBuffer.getBuffer().flip();
        networkBuffer.mark();

        byte[] bytes = new byte[3];

        for (int i = 0; i < bytes.length; ++i) {
            bytes[i] = networkBuffer.readByte();

            if (bytes[i] >= 0) {
                NetworkBuffer packetBuffer = new NetworkBuffer(ByteBuffer.wrap(bytes));

                int length = packetBuffer.readVarInt();

                System.out.println("Before: " + networkBuffer.readableBytes());
                if (networkBuffer.readableBytes() >= length && length > 0) {
                    byte[] bytes1 = networkBuffer.readBytes(length);
                    System.out.println("Readed bytes: " + Arrays.toString(bytes1));
                    System.out.println("remaining: " + networkBuffer.readableBytes());
                    return ByteBuffer.wrap(bytes1);
                }

                networkBuffer.reset();
            }
        }

        networkBuffer.reset();
        return null;
    }

    @Override
    public void handleException(PlayerConnection connection, Exception exception) {
        LOGGER.error(exception);
        connection.getChannel().close();
    }
}
