package net.echo.sparky.network.state;

import net.echo.sparky.network.packet.Packet;

@FunctionalInterface
public interface PacketFactory<T extends Packet> {
    T create();
}
