package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientArmSwing implements Packet.Client {

    public ClientArmSwing() {
    }

    @Override
    public void read(NetworkBuffer buffer) {

    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handleArmSwing(this);
    }
}
