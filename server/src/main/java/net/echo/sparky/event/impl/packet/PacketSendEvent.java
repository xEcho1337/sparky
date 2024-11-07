package net.echo.sparky.event.impl.packet;

import net.echo.sparky.event.Cancellable;
import net.echo.sparky.event.Event;
import net.echo.sparky.network.packet.Packet;

public class PacketSendEvent extends Cancellable implements Event {

    private final Packet.Server packet;

    public PacketSendEvent(Packet.Server packet) {
        this.packet = packet;
    }

    public Packet.Server getPacket() {
        return packet;
    }
}
