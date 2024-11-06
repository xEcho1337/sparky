package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.utils.ThreadScheduleUtils;

public class ClientPlayerIdle implements Packet.Client {

    private boolean onGround;

    public ClientPlayerIdle() {
    }

    public ClientPlayerIdle(boolean onGround) {
        this.onGround = onGround;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.onGround = buffer.readBoolean();
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handleIdle(this);
    }

    public boolean isOnGround() {
        return onGround;
    }
}
