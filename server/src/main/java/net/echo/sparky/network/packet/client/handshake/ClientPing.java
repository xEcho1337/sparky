package net.echo.sparky.network.packet.client.handshake;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientPing implements Packet.Client {

    private long id;

    public ClientPing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readLong();
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handlePing(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
