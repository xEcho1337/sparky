package net.echo.sparky.network.packet.client.play;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientKeepAlive implements Packet.Client {

    private long id;

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readVarInt();
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handleKeepAlive(this);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
