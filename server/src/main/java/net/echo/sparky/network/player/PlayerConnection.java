package net.echo.sparky.network.player;

import net.echo.server.channel.Channel;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.impl.packet.PacketSendEvent;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.login.ServerLoginDisconnect;
import net.echo.sparky.network.packet.server.play.ServerDisconnect;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.player.SparkyPlayer;
import net.kyori.adventure.text.TextComponent;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class PlayerConnection {

    private final Channel channel;
    private final SparkyPlayer player;

    public PlayerConnection(Channel channel) {
        this.channel = channel;
        this.player = new SparkyPlayer(this);
    }

    public void sendPacket(Packet.Server packet) {
        if (channel == null || !channel.isOpen()) {
            LOGGER.error("Channel is null or closed!");
            return;
        }

        PacketSendEvent event = new PacketSendEvent(packet);

        MinecraftServer.getInstance().getEventHandler().call(event);

        if (event.isCancelled()) return;

        channel.write(packet);
    }

    public void close(TextComponent reason) {
        ConnectionState state = channel.getAttribute(NetworkManager.CONNECTION_STATE).getValue();

        Packet.Server packet = state == ConnectionState.LOGIN
                ? new ServerLoginDisconnect(reason)
                : new ServerDisconnect(reason);

        if (channel.isOpen()) {
            channel.writeAndFlush(packet);
        }

        channel.close();
    }

    public void flushPacket(Packet.Server packet) {
        if (channel == null || !channel.isOpen()) return;

        channel.writeAndFlush(packet);
    }

    public Channel getChannel() {
        return channel;
    }

    public SparkyPlayer getPlayer() {
        return player;
    }
}