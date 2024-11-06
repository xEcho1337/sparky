package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientUseEntity implements Packet.Client {

    private int entityId;
    private InteractType interactType;
    private float targetX;
    private float targetY;
    private float targetZ;

    public ClientUseEntity() {
    }

    public ClientUseEntity(int entityId, InteractType interactType, float targetX, float targetY, float targetZ) {
        this.entityId = entityId;
        this.interactType = interactType;
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.entityId = buffer.readVarInt();
        this.interactType = InteractType.values()[buffer.readVarInt()];

        if (interactType == InteractType.INTERACT_AT) {
            this.targetX = buffer.readFloat();
            this.targetY = buffer.readFloat();
            this.targetZ = buffer.readFloat();
        }
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handleUseEntity(this);
    }

    public enum InteractType {
        INTERACT, ATTACK, INTERACT_AT
    }

    public int getEntityId() {
        return entityId;
    }

    public InteractType getInteractType() {
        return interactType;
    }

    public float getTargetX() {
        return targetX;
    }

    public float getTargetY() {
        return targetY;
    }

    public float getTargetZ() {
        return targetZ;
    }
}
