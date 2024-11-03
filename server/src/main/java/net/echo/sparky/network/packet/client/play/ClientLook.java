package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.utils.ThreadScheduleUtils;

public class ClientLook extends ClientPositionIdle {

    private float yaw;
    private float pitch;

    public ClientLook() {
    }

    public ClientLook(float yaw, float pitch, boolean onGround) {
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
    public void handle(MinecraftServer server, PlayerConnection connection) {
        if (!ThreadScheduleUtils.ensureMainThread(this, server, connection)) return;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
