package net.echo.sparky.network.packet.server.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ServerDisconnect implements Packet.Server {

    private Component reason;

    public ServerDisconnect() {
    }

    public ServerDisconnect(Component reason) {
        this.reason = reason;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(GsonComponentSerializer.gson().serialize(this.reason));
    }

    public Component getReason() {
        return reason;
    }
}
