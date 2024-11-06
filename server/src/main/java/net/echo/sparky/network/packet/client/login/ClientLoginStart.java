package net.echo.sparky.network.packet.client.login;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.player.SparkyPlayer;
import net.echo.sparky.world.World;

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
    public void handle(PacketHandlerProcessor processor) {
        processor.handleLoginStart(this);
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
