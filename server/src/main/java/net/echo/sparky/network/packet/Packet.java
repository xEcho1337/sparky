package net.echo.sparky.network.packet;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;

/**
 * Abstract reference to a Minecraft packet.
 */
public interface Packet {

    /**
     * The packet is sent by the client.
     */
    interface Client extends Packet {
        void read(NetworkBuffer buffer);

        void handle(MinecraftServer server, ChannelHandlerContext context);
    }

    /**
     * The packet is sent by the server.
     */
    interface Server extends Packet {
        void write(NetworkBuffer buffer);
    }
}
