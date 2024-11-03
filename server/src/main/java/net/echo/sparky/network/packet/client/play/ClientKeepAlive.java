package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.utils.ThreadScheduleUtils;

public class ClientKeepAlive implements Packet.Client {

    private long id;

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readVarInt();
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {
        if (!ThreadScheduleUtils.ensureMainThread(this, server, connection)) return;


    }
}
