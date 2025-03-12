package net.echo.sparky.network.packet;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;

/**
 * Abstract reference to a Minecraft packet.
 */
public interface Packet {

    /**
     * The packet is sent by the client.
     */
    interface Client extends Packet {
        void read(NetworkBuffer buffer);

        void handle(PacketProcessor processor);
    }

    /**
     * The packet is sent by the server.
     */
    interface Server extends Packet {
        void write(NetworkBuffer buffer);
    }
}
