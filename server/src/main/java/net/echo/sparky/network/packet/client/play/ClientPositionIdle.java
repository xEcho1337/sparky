package net.echo.sparky.network.packet.client.play;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.utils.ThreadScheduleUtils;

public class ClientPositionIdle implements Packet.Client {

    private boolean onGround;

    public ClientPositionIdle() {
    }

    public ClientPositionIdle(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.onGround = buffer.readBoolean();
    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {
        if (!ThreadScheduleUtils.ensureMainThread(this, server, context)) return;
    }

    public boolean isOnGround() {
        return onGround;
    }
}
