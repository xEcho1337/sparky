package net.echo.sparky.event.impl.packet;

import net.echo.sparky.event.Cancellable;
import net.echo.sparky.event.Event;
import net.echo.sparky.network.packet.Packet;

public class PacketReceiveEvent extends Cancellable implements Event {

    private final Packet.Client packet;

    public PacketReceiveEvent(Packet.Client packet) {
        this.packet = packet;
    }

    public Packet.Client getPacket() {
        return packet;
    }
}
