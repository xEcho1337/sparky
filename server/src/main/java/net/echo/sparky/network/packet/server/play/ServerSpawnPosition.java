package net.echo.sparky.network.packet.server.play;

import net.echo.sparky.math.Vector3i;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerSpawnPosition implements Packet.Server {

    private Vector3i position;

    public ServerSpawnPosition() {
    }

    public ServerSpawnPosition(Vector3i position) {
        this.position = position;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writePosition(position);
    }
}
