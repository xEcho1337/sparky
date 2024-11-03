package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientEntityAction implements Packet.Client {

    private int entityId;
    private Action action;
    private int actionValue;

    public ClientEntityAction() {
    }

    public ClientEntityAction(int entityId, Action action, int actionValue) {
        this.entityId = entityId;
        this.action = action;
        this.actionValue = actionValue;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.entityId = buffer.readVarInt();
        this.action = Action.values()[buffer.readVarInt()];
        this.actionValue = buffer.readVarInt();
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {

    }

    public enum Action {
        START_SNEAKING, STOP_SNEAKING, LEAVE_BED, START_SPRINTING, STOP_SPRINTING, JUMP_WITH_HORSE, OPEN_HORSE_INVENTORY
    }

    public int getEntityId() {
        return entityId;
    }

    public Action getAction() {
        return action;
    }

    public int getActionValue() {
        return actionValue;
    }
}
