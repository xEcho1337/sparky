package net.echo.sparky.network.packet.client.play;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ClientHeldItemChange implements Packet.Client {

    private short slot;

    public ClientHeldItemChange() {
    }

    public ClientHeldItemChange(short slot) {
        this.slot = slot;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.slot = buffer.readShort();
    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {
    }
}
