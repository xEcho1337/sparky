package net.echo.sparky.network.packet.client.login;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientLoginStart implements Packet.Client {

    private String name;

    public ClientLoginStart() {
    }

    public ClientLoginStart(String name) {
        this.name = name;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.name = buffer.readString();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleLoginStart(this);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
