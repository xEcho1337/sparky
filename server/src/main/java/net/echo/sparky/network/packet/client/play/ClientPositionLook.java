package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.utils.ThreadScheduleUtils;

public class ClientPositionLook extends ClientPositionIdle {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;

    public ClientPositionLook() {
    }

    public ClientPositionLook(double x, double y, double z, float yaw, float pitch, boolean onGround) {
        super(onGround);
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        this.yaw = buffer.readFloat();
        this.pitch = buffer.readFloat();
        super.read(buffer);
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {
        if (!ThreadScheduleUtils.ensureMainThread(this, server, connection)) return;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }
}
