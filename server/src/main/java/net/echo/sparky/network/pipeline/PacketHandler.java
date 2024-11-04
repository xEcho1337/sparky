package net.echo.sparky.network.pipeline;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class PacketHandler extends SimpleChannelInboundHandler<Packet.Client> {

    private final NetworkManager networkManager;
    private final MinecraftServer server;
    private PlayerConnection connection;

    public PacketHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.server = networkManager.getServer();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet.Client packet) {
        packet.handle(server, connection);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        this.connection = new PlayerConnection(context.channel());

        networkManager.getConnectionManager().addConnection(connection);

        context.channel().attr(NetworkManager.CONNECTION_STATE).set(ConnectionState.HANDSHAKING);
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        ConnectionState state = context.channel().attr(NetworkManager.CONNECTION_STATE).get();

        if (connection == null) return;

        networkManager.getConnectionManager().removeConnection(connection);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        TextComponent reason = Component.text(cause.getMessage()).color(NamedTextColor.RED);

        connection.close(reason);
    }

    public MinecraftServer getServer() {
        return server;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
