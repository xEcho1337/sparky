package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.Listenable;
import net.echo.sparky.event.impl.AsyncChatEvent;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.play.ServerChatMessage;
import net.echo.sparky.network.player.PlayerConnection;
import net.kyori.adventure.text.Component;

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
    public void handle(MinecraftServer server, PlayerConnection connection) {
        Listenable event = new AsyncChatEvent(message);

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        Component component = Component.text(String.format("<%s> %s", connection.getName(), message));

        connection.sendPacket(new ServerChatMessage(component, ServerChatMessage.MessageType.CHAT));
    }
}
