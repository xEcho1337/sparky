package net.echo.sparky.network.packet.client;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.Listenable;
import net.echo.sparky.event.impl.AsyncHandshakeEvent;
import net.echo.sparky.event.impl.AsyncHandshakeEvent.*;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ClientHandshake implements Packet.Client {

    private String address;
    private int protocolVersion;
    private int port;
    private HandshakeState nextState;

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

    @Override
    public void read(NetworkBuffer buffer) {
        this.protocolVersion = buffer.readVarInt();
        this.address = buffer.readString();
        this.port = buffer.readUnsignedShort();
        this.nextState = HandshakeState.values()[buffer.readVarInt() - 1];
    }

    @Override
    public boolean handle(MinecraftServer server) {
        Listenable event = new AsyncHandshakeEvent(address, port, protocolVersion, nextState);

        server.getEventHandler().call(event);

        return event.isCancelled();
    }
}
