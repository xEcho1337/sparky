package net.echo.sparky.network.packet.client.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;

public class ClientPlayerLook extends ClientPlayerIdle {

    private float yaw;
    private float pitch;

    public ClientPlayerLook() {
    }

    public ClientPlayerLook(float yaw, float pitch, boolean onGround) {
        super(onGround);
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.yaw = buffer.readFloat();
        this.pitch = buffer.readFloat();
        super.read(buffer);
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleLook(this);
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
