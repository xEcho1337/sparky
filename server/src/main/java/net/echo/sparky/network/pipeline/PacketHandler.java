package net.echo.sparky.network.pipeline;

import net.echo.server.attributes.Attribute;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.handler.InboundHandler;
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

import java.util.HashMap;
import java.util.Map;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class PacketHandler implements InboundHandler<PlayerConnection, Packet.Client> {

    private final NetworkManager networkManager;
    private final MinecraftServer server;
    private final Map<PlayerConnection, PacketHandlerProcessor> processorMap;

    public PacketHandler(NetworkManager networkManager) {
        this.networkManager = networkManager;
        this.server = networkManager.getServer();
        this.processorMap = new HashMap<>();
    }

    @Override
    public void onChannelConnect(PlayerConnection connection) {
        processorMap.put(connection, new PacketHandlerProcessor(server, connection));
        networkManager.getConnectionManager().addConnection(connection);

        Channel channel = connection.getChannel();
        channel.setAttribute(NetworkManager.CONNECTION_STATE, ConnectionState.HANDSHAKING);

        Attribute<ConnectionState> state = channel.getAttribute(NetworkManager.CONNECTION_STATE);
        state.setValue(ConnectionState.HANDSHAKING);
    }

    @Override
    public void onChannelDisconnect(PlayerConnection connection) {
        server.getPlayerList().remove(connection.getPlayer());
        networkManager.getConnectionManager().removeConnection(connection);
        processorMap.remove(connection);
    }

    @Override
    public void handle(PlayerConnection connection, Packet.Client input) {
        PacketHandlerProcessor processor = processorMap.get(connection);
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
