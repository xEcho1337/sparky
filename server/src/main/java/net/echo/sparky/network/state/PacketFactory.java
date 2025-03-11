package net.echo.sparky.network.state;

import net.echo.sparky.network.packet.Packet;

/**
 * A functional interface used to create packets.
 * @param <T> the type of the packet
 */
@FunctionalInterface
public interface PacketFactory<T extends Packet> {

    /**
     * Creates a new instance of the packet.
     * @return a new packet instance
     */
    T create();
}
