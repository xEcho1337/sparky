package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientHeldItemChange implements Packet.Client {

    private short slot;

    public ClientHeldItemChange() {
    }

    public ClientHeldItemChange(short slot) {
        this.slot = slot;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.slot = buffer.readShort();
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {
    }
}
