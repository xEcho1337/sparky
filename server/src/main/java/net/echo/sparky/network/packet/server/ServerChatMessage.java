package net.echo.sparky.network.packet.server;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ServerChatMessage implements Packet.Server {

    private final Component component;
    private final MessageType messageType;

    public ServerChatMessage(Component component, MessageType messageType) {
        this.component = component;
        this.messageType = messageType;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(GsonComponentSerializer.gson().serialize(component));
        buffer.writeByte(messageType.ordinal());
    }

    public enum MessageType {
        CHAT, SYSTEM_MESSAGE, HOTBAR
    }
}
