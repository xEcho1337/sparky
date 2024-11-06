package net.echo.sparky.network.packet;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.player.PlayerConnection;

/**
 * Abstract reference to a Minecraft packet.
 */
public interface Packet {

    /**
     * The packet is sent by the client.
     */
    interface Client extends Packet {
        void read(NetworkBuffer buffer);

        void handle(PacketHandlerProcessor processor);
    }

    /**
     * The packet is sent by the server.
     */
    interface Server extends Packet {
        void write(NetworkBuffer buffer);
    }
}
