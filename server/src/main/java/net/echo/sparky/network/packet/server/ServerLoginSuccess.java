package net.echo.sparky.network.packet.server;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

import java.util.UUID;

public class ServerLoginSuccess implements Packet.Server {

    private final UUID uniqueId;
    private final String username;

    public ServerLoginSuccess(UUID uniqueId, String username) {
        this.uniqueId = uniqueId;
        this.username = username;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(uniqueId.toString());
        buffer.writeString(username);
    }
}
