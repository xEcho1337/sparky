package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientStatus implements Packet.Client {

    private Action action;

    public ClientStatus() {
    }

    public ClientStatus(Action action) {
        this.action = action;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.action = Action.values()[buffer.readVarInt()];
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {

    }

    public enum Action {
        PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY
    }

    public Action getAction() {
        return action;
    }
}
