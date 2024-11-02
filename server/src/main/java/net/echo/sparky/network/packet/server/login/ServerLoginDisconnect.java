package net.echo.sparky.network.packet.server.login;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ServerLoginDisconnect implements Packet.Server {

    private TextComponent reason;

    public ServerLoginDisconnect() {
    }

    public ServerLoginDisconnect(TextComponent reason) {
        this.reason = reason;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(GsonComponentSerializer.gson().serialize(reason));
    }
}
