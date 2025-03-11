package net.echo.sparky.network.pipeline;

import net.echo.server.attributes.Attribute;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.transmitters.InboundHandler;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.impl.packet.PacketReceiveEvent;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class PacketHandler implements InboundHandler<PlayerConnection, Packet.Client> {

    private final NetworkManager networkManager;
    private final MinecraftServer server;
    private PacketHandlerProcessor processor;

    public PacketHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.server = networkManager.getServer();
    }

    @Override
    public void onChannelConnect(PlayerConnection connection) {
        this.processor = new PacketHandlerProcessor(server, connection);

        networkManager.getConnectionManager().addConnection(connection);

        connection.getChannel().setAttribute(NetworkManager.CONNECTION_STATE);

        Attribute<ConnectionState> state = connection.getChannel().getAttribute(NetworkManager.CONNECTION_STATE);

        state.setValue(ConnectionState.HANDSHAKING);
    }

    @Override
    public void onChannelDisconnect(PlayerConnection connection) {
        server.getPlayerList().remove(connection.getPlayer());
        networkManager.getConnectionManager().removeConnection(connection);
    }

    @Override
    public void read(PlayerConnection connection, Packet.Client input) {
        Channel channel = connection.getChannel();

        if (channel == null || !channel.isOpen()) return;

        PacketReceiveEvent event = new PacketReceiveEvent(input);
        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        input.handle(processor);
    }

    @Override
    public void handleException(PlayerConnection connection, Exception exception) {
        LOGGER.error(exception);

        TextComponent reason = Component.text(exception.getMessage()).color(NamedTextColor.RED);
        connection.close(reason);
    }

    public MinecraftServer getServer() {
        return server;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }
}
