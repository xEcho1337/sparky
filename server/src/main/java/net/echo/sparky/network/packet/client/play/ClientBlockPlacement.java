package net.echo.sparky.network.packet.client.play;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.math.Vector3i;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.enums.Facing;

// TODO
public class ClientBlockPlacement implements Packet.Client {

    private Vector3i position;
    private Facing facing;


    public ClientBlockPlacement() {
    }

    public ClientBlockPlacement(Vector3i position, Facing facing) {
        this.position = position;
        this.facing = facing;
    }

    @Override
    public void read(NetworkBuffer buffer) {

    }

    @Override
    public void handle(MinecraftServer server, ChannelHandlerContext context) {

    }
}
