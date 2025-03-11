package net.echo.sparky.network.packet.client.play;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientArmSwing implements Packet.Client {

    public ClientArmSwing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {

    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handleArmSwing(this);
    }
}
