package net.echo.sparky.network.packet.client.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.handler.PacketProcessor;
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
        int length = buffer.remaining();

        byte[] data = new byte[length];
        buffer.getBuffer().get(data, 0, data.length);

        NetworkBuffer networkBuffer = new NetworkBuffer(length);

        networkBuffer.writeBytes(data);
        networkBuffer.getBuffer().flip();

        this.data = networkBuffer;
    }

    @Override
    public void handle(PacketProcessor processor) {
        processor.handlePluginMessage(this);
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
