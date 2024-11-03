package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparkyapi.world.PlayerAbilities;

public class ClientPlayerAbilities implements Packet.Client {

    private PlayerAbilities abilities;
    private float flySpeed;
    private float walkingSpeed;

    public ClientPlayerAbilities() {
    }

    public ClientPlayerAbilities(PlayerAbilities abilities, float flySpeed, float walkingSpeed) {
        this.abilities = abilities;
        this.flySpeed = flySpeed;
        this.walkingSpeed = walkingSpeed;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.abilities = new PlayerAbilities(buffer.readByte());
        this.flySpeed = buffer.readFloat();
        this.walkingSpeed = buffer.readFloat();
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {

    }

    public PlayerAbilities getAbilities() {
        return abilities;
    }

    public float getFlySpeed() {
        return flySpeed;
    }

    public float getWalkingSpeed() {
        return walkingSpeed;
    }
}
