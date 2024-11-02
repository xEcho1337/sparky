package net.echo.sparky.network.packet.client.handshake;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.Listenable;
import net.echo.sparky.event.impl.AsyncHandshakeEvent;
import net.echo.sparky.event.impl.AsyncHandshakeEvent.*;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.state.ConnectionState;

public class ClientHandshake implements Packet.Client {

    private String address;
    private int protocolVersion;
    private int port;
    private HandshakeState nextState;

    public ClientHandshake() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.protocolVersion = buffer.readVarInt();
        this.address = buffer.readString();
        this.port = buffer.readUnsignedShort();
        this.nextState = HandshakeState.values()[buffer.readVarInt() - 1];
    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {
        Listenable event = new AsyncHandshakeEvent(address, port, protocolVersion, nextState);

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        ConnectionState nextConnectionState = switch (nextState) {
            case LOGIN -> ConnectionState.LOGIN;
            case STATUS -> ConnectionState.STATUS;
        };

        context.channel().attr(NetworkManager.CONNECTION_STATE).set(nextConnectionState);
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public String getAddress() {
        return address;
    }

    public int getPort() {
        return port;
    }

    public HandshakeState getNextState() {
        return nextState;
    }
}
