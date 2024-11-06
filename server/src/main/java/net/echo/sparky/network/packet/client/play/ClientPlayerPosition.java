package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.utils.ThreadScheduleUtils;

public class ClientPlayerPosition extends ClientPlayerIdle {

    private double x;
    private double y;
    private double z;

    public ClientPlayerPosition() {
    }

    public ClientPlayerPosition(double x, double y, double z, boolean onGround) {
        super(onGround);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.x = buffer.readDouble();
        this.y = buffer.readDouble();
        this.z = buffer.readDouble();
        super.read(buffer);
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handlePosition(this);
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
}
