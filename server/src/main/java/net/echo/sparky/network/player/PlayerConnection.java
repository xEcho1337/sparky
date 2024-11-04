package net.echo.sparky.network.player;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.login.ServerLoginDisconnect;
import net.echo.sparky.network.packet.server.play.ServerDisconnect;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.player.SparkyPlayer;
import net.kyori.adventure.text.TextComponent;
import org.apache.logging.log4j.core.util.ArrayUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

public class PlayerConnection {

    private final ConcurrentMap<Packet.Server, List<Runnable>> packetQueue;
    private final Channel channel;
    private final SparkyPlayer player;

    public PlayerConnection(Channel channel) {
        this.channel = channel;
        this.player = new SparkyPlayer(this);
        this.packetQueue = new ConcurrentHashMap<>();
    }

    public void sendPacket(Packet.Server packet) {
        var alreadyCallbacks = packetQueue.get(packet);

        if (alreadyCallbacks == null) {
            alreadyCallbacks = new ArrayList<>();
        }

        this.packetQueue.put(packet, alreadyCallbacks);
    }

    public void sendPacket(Packet.Server packet, Runnable... callbacks) {
        var alreadyCallbacks = packetQueue.get(packet);

        if (alreadyCallbacks == null) {
            alreadyCallbacks = new ArrayList<>();
        }

        if (callbacks != null) {
            alreadyCallbacks.addAll(List.of(callbacks));
        }

        this.packetQueue.put(packet, alreadyCallbacks);
    }

    public Map<Packet.Server, List<Runnable>> getPacketQueue() {
        return packetQueue;
    }

    public Channel getChannel() {
        return channel;
    }

    public SparkyPlayer getPlayer() {
        return player;
    }

    public void close(TextComponent reason) {
        ConnectionState state = channel.attr(NetworkManager.CONNECTION_STATE).get();

        Packet.Server packet = state == ConnectionState.LOGIN
                ? new ServerLoginDisconnect(reason)
                : new ServerDisconnect(reason);

        channel.writeAndFlush(packet);
        channel.close();
    }

    public void dispatchPacket(Packet.Server packet, List<Runnable> callbacks) {
        ChannelFuture future = channel.writeAndFlush(packet);

        for (Runnable callback : callbacks) {
            if (callback == null) continue;

            future.addListener(x -> callback.run());
        }

        future.addListener(FIRE_EXCEPTION_ON_FAILURE);
    }

    public void dispatchPacket(Packet.Server packet, Runnable callback) {
        ChannelFuture future = channel.writeAndFlush(packet);

        future.addListener(x -> callback.run());

        future.addListener(FIRE_EXCEPTION_ON_FAILURE);
    }
}