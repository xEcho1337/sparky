package net.echo.sparky.network.packet.client.play;

import net.echo.server.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

public class ClientPluginMessage implements Packet.Client {

    private String channel;
    private NetworkBuffer data;

    public ClientPluginMessage() {
    }

    public ClientPluginMessage(String channel, NetworkBuffer data) {
        this.channel = channel;
        this.data = data;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.channel = buffer.readString();
        this.data = new NetworkBuffer(buffer.readBytes(buffer.readableBytes()));
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handlePluginMessage(this);
        data.release();
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public NetworkBuffer getData() {
        return data;
    }

    public void setData(NetworkBuffer data) {
        this.data = data;
    }
}
