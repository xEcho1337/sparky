package net.echo.sparky.network.packet.client.handshake;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientStatusRequest implements Packet.Client {

    public ClientStatusRequest() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handleStatusRequest(this);
    }
}
