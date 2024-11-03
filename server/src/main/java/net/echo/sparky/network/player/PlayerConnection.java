package net.echo.sparky.network.player;

import io.netty.channel.Channel;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.login.ServerLoginDisconnect;
import net.echo.sparky.network.packet.server.play.ServerDisconnect;
import net.echo.sparky.network.state.ConnectionState;
import net.kyori.adventure.text.TextComponent;

import java.util.LinkedHashMap;
import java.util.UUID;

public class PlayerConnection {

    private final LinkedHashMap<Packet.Server, GenericFutureListener<? extends Future<? super Void>>> packetQueue;
    private final Channel channel;

    private String name;
    private UUID uuid;

    public PlayerConnection(Channel channel) {
        this.channel = channel;
        this.packetQueue = new LinkedHashMap<>();
    }

    public synchronized void sendPacket(Packet.Server packet) {
        this.packetQueue.put(packet, null);
    }

    public synchronized void sendPacket(Packet.Server packet, GenericFutureListener<? extends Future<? super Void>> callback) {
        this.packetQueue.put(packet, callback);
    }

    public synchronized LinkedHashMap<Packet.Server, GenericFutureListener<? extends Future<? super Void>>> getPacketQueue() {
        return packetQueue;
    }

    public Channel getChannel() {
        return channel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PlayerConnection setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public void close(TextComponent reason) {
        ConnectionState state = channel.attr(NetworkManager.CONNECTION_STATE).get();

        Packet.Server packet = switch (state) {
            case LOGIN -> new ServerLoginDisconnect(reason);
            default -> new ServerDisconnect(reason);
        };

        channel.writeAndFlush(packet);
        channel.close();
    }
}
