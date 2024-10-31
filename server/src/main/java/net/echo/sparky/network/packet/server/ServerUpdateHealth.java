package net.echo.sparky.network.packet.server;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerUpdateHealth implements Packet.Server {

    private final float health;
    private final int food;
    private final float saturation;

    public ServerUpdateHealth(float health, int food, float saturation) {
        this.health = health;
        this.food = food;
        this.saturation = saturation;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeFloat(health);
        buffer.writeVarInt(food);
        buffer.writeFloat(saturation);
    }
}
