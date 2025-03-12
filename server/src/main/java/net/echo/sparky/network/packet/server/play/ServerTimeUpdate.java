package net.echo.sparky.network.packet.server.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerTimeUpdate implements Packet.Server {

    private int worldAge;
    private int timeOfDay;

    public ServerTimeUpdate() {
    }

    public ServerTimeUpdate(int worldAge, int timeOfDay) {
        this.worldAge = worldAge;
        this.timeOfDay = timeOfDay;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeLong(worldAge);
        buffer.writeLong(timeOfDay);
    }
}
