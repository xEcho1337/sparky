package net.echo.sparky.network.packet.client;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.Listenable;
import net.echo.sparky.event.impl.AsyncLoginStartEvent;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ClientLoginStart implements Packet.Client {

    private String name;

    @Override
    public void read(NetworkBuffer buffer) {
        this.name = buffer.readString();
    }

    @Override
    public boolean handle(MinecraftServer server) {
        Listenable event = new AsyncLoginStartEvent(name);

        server.getEventHandler().call(event);

        return event.isCancelled();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
