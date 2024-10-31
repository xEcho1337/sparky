package net.echo.sparky.network.packet;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;

/**
 * Abstract reference to a Minecraft packet.
 */
public interface Packet {

    /**
     * The packet is sent by the client.
     */
    interface Client {
        void read(NetworkBuffer buffer);

        boolean handle(MinecraftServer server);
    }

    /**
     * The packet is sent by the server.
     */
    interface Server {
        void write(NetworkBuffer buffer);
    }
}
