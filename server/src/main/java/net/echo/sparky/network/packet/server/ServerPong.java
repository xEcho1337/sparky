package net.echo.sparky.network.packet.server;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerPong implements Packet.Server {

    private final long id;

    public ServerPong(long id) {
        this.id = id;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeLong(id);
    }
}
