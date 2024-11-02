package net.echo.sparky.network.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.state.ConnectionState;

public class PacketHandler extends SimpleChannelInboundHandler<Packet.Client> {

    private final MinecraftServer server;

    public PacketHandler(MinecraftServer server) {
        this.server = server;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet.Client packet) {
        packet.handle(server, context);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        context.channel().attr(NetworkManager.CONNECTION_STATE).set(ConnectionState.HANDSHAKING);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        ConnectionState state = context.channel().attr(NetworkManager.CONNECTION_STATE).get();

        System.out.println("Disconnected at " + state);
        if (state != ConnectionState.PLAY) return;

        // server.removePlayer(context);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace(System.err);
        //context.close();
    }

    public MinecraftServer getServer() {
        return server;
    }
}
