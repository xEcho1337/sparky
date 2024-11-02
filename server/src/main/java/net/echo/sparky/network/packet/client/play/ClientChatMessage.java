package net.echo.sparky.network.packet.client.play;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.utils.ThreadScheduleUtils;

public class ClientChatMessage implements Packet.Client {

    private String message;

    public ClientChatMessage() {
    }

    public ClientChatMessage(String message) {
        this.message = message;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.message = buffer.readString();
    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {
        if (!ThreadScheduleUtils.ensureMainThread(this, server, context)) return;


    }
}
