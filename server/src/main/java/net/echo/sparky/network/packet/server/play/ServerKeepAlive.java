package net.echo.sparky.network.packet.server.play;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerKeepAlive implements Packet.Server {

    private int id;

    public ServerKeepAlive() {
    }

    public ServerKeepAlive(int id) {
        this.id = id;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeVarInt(id);
    }
}
