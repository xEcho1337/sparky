package net.echo.sparky.event.impl;

import net.echo.sparky.event.Cancellable;

public class AsyncHandshakeEvent extends Cancellable {

    public enum HandshakeState {
        STATUS, LOGIN
    }

    private String address;
    private int port;
    private int protocolVersion;
    private HandshakeState state;

    public AsyncHandshakeEvent(String address, int port, int protocolVersion, HandshakeState state) {
        this.address = address;
        this.port = port;
        this.protocolVersion = protocolVersion;
        this.state = state;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getProtocolVersion() {
        return protocolVersion;
    }

    public void setProtocolVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }

    public HandshakeState getState() {
        return state;
    }

    public void setState(HandshakeState state) {
        this.state = state;
    }
}
