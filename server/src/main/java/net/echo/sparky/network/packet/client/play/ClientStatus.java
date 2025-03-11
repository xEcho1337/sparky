package net.echo.sparky.network.packet.client.play;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientStatus implements Packet.Client {

    public enum Action {
        PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY
    }

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
    public void handle(PacketHandlerProcessor processor) {
        processor.handleStatus(this);
    }

    public Action getAction() {
        return action;
    }
}
