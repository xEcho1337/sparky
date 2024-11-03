package net.echo.sparky.network.packet.client.handshake;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.handshake.ServerPong;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientPing implements Packet.Client {

    private long id;

    public ClientPing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readLong();
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {
    }
}
