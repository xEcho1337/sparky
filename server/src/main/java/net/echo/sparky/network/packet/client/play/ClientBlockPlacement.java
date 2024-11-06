package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.math.Vector3i;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
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
    public void handle(PacketHandlerProcessor processor) {
        processor.handleBlockPlacement(this);
    }

    public Vector3i getPosition() {
        return position;
    }

    public void setPosition(Vector3i position) {
        this.position = position;
    }

    public Facing getFacing() {
        return facing;
    }

    public void setFacing(Facing facing) {
        this.facing = facing;
    }
}
