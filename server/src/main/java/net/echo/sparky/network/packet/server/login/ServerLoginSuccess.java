package net.echo.sparky.network.packet.server.login;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

import java.util.UUID;

public class ServerLoginSuccess implements Packet.Server {

    private UUID uniqueId;
    private String username;

    public ServerLoginSuccess() {
    }

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
