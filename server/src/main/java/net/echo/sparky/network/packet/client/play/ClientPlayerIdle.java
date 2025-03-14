package net.echo.sparky.network.packet.client.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;
import net.echo.sparky.network.packet.Packet;

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
    public void handle(PacketProcessor processor) {
        processor.handleIdle(this);
    }

    public boolean isOnGround() {
        return onGround;
    }
}
