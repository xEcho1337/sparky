package net.echo.sparky.network.packet.client.play;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ClientArmSwing implements Packet.Client {

    public ClientArmSwing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {

    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {

    }
}
