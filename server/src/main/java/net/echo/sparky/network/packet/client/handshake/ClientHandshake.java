package net.echo.sparky.network.packet.client.handshake;

import net.echo.sparky.event.impl.async.AsyncHandshakeEvent.HandshakeState;
import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;
import net.echo.sparky.network.packet.Packet;

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
    public void handle(PacketProcessor processor) {
        processor.handleHandshake(this);
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
