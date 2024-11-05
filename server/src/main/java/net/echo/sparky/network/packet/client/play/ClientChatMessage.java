package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.Cancellable;
import net.echo.sparky.event.impl.AsyncChatEvent;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.play.ServerChatMessage;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.player.SparkyPlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

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
        Cancellable event = new AsyncChatEvent(message);

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        SparkyPlayer player = connection.getPlayer();

        TextComponent component = Component.text(String.format("<%s> %s", player.getName(), message));

        for (SparkyPlayer other : server.getPlayerList()) {
            other.sendMessage(component);
        }
    }
}
