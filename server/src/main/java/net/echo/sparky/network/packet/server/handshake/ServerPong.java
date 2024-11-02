package net.echo.sparky.network.packet.server.handshake;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerPong implements Packet.Server {

    private long id;

    public ServerPong() {
    }

    public ServerPong(long id) {
        this.id = id;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeLong(id);
    }
}
