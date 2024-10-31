package net.echo.sparky.network.packet.server;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerTimeUpdate implements Packet.Server {

    private final int worldAge;
    private final int timeOfDay;

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
