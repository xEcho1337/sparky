package net.echo.sparky.network.packet.server.play;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.world.RelativeFlag;

public class ServerPositionAndLook implements Packet.Server {

    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private RelativeFlag flags;

    public ServerPositionAndLook() {
    }

    public ServerPositionAndLook(double x, double y, double z, float yaw, float pitch, RelativeFlag flags) {
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
