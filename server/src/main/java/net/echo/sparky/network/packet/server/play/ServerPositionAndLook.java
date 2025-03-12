package net.echo.sparky.network.packet.server.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.world.Location;
import net.echo.sparkyapi.flags.impl.TeleportFlag;

public class ServerPositionAndLook implements Packet.Server {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private TeleportFlag flags;

    public ServerPositionAndLook() {
    }

    public ServerPositionAndLook(Location location, TeleportFlag flags) {
        this(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch(), flags);
    }

    public ServerPositionAndLook(double x, double y, double z, float yaw, float pitch, TeleportFlag flags) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.flags = flags;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeFloat(yaw);
        buffer.writeFloat(pitch);
        buffer.writeByte(flags.getMask());
    }
}
