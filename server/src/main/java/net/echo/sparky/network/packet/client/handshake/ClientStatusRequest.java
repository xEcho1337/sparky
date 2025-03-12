package net.echo.sparky.network.packet.client.handshake;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientStatusRequest implements Packet.Client {

    public ClientStatusRequest() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleStatusRequest(this);
    }
}
