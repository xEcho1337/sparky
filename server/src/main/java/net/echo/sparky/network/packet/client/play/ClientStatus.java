package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

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
    public void handle(PacketHandlerProcessor processor) {
        processor.handleStatus(this);
    }

    public enum Action {
        PERFORM_RESPAWN, REQUEST_STATS, OPEN_INVENTORY
    }

    public Action getAction() {
        return action;
    }
}
