package net.echo.sparky.network.packet.client.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientChatMessage implements Packet.Client {

    private String message;

    public ClientChatMessage() {
    }

    public ClientChatMessage(String message) {
        this.message = message;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.message = buffer.readString();
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handleChatMessage(this);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
