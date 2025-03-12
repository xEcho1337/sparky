package net.echo.sparky.network.packet.server.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;

public class ServerUpdateHealth implements Packet.Server {

    private float health;
    private int food;
    private float saturation;

    public ServerUpdateHealth() {
    }

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
