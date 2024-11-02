package net.echo.sparky.network.packet.client.play;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ClientPluginMessage implements Packet.Client {

    private String channel;
    private NetworkBuffer data;

    public ClientPluginMessage() {
    }

    public ClientPluginMessage(String channel, NetworkBuffer data) {
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.channel = buffer.readString();
        this.data = new NetworkBuffer(buffer.readBytes(buffer.readableBytes()));
    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {
        data.release();
    }

    public String getChannel() {
        return channel;
    }

    public NetworkBuffer getData() {
        return data;
    }
}
