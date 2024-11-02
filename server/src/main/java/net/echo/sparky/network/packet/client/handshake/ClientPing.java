package net.echo.sparky.network.packet.client.handshake;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.handshake.ServerPong;

public class ClientPing implements Packet.Client {

    private long id;

    public ClientPing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.id = buffer.readLong();
    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {
        context.channel().writeAndFlush(new ServerPong(id));
    }
}
