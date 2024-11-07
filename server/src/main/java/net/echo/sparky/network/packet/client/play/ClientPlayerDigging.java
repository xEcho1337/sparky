package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.math.Vector3i;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.enums.Facing;

public class ClientPlayerDigging implements Packet.Client {

    private DiggingStatus status;
    private Vector3i position;
    private Facing facing;

    @Override
    public void read(NetworkBuffer buffer) {
        this.status = DiggingStatus.values()[buffer.readVarInt()];
        this.position = buffer.readPosition();
        this.facing = Facing.values()[buffer.readVarInt()];
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handlePlayerDigging(this);
    }

    public enum DiggingStatus {
        STARTED_DIGGING, CANCELLED_DIGGING, FINISHED_DIGGING, DROP_ITEM_STACK, DROP_ITEM, STOP_USING_ITEM
    }

    public DiggingStatus getStatus() {
        return status;
    }

    public Vector3i getPosition() {
        return position;
    }

    public Facing getFacing() {
        return facing;
    }
}
