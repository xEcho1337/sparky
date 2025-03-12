package net.echo.sparky.network.packet.client.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientArmSwing implements Packet.Client {

    public ClientArmSwing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {

    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleArmSwing(this);
    }
}
