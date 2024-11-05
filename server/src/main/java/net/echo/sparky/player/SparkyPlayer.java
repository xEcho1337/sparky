package net.echo.sparky.player;

import net.echo.sparky.network.packet.server.play.ServerChatMessage;
import net.echo.sparky.network.player.PlayerConnection;
import net.kyori.adventure.text.TextComponent;

import java.util.UUID;

public class SparkyPlayer {

    private final PlayerConnection connection;

    private String name;
    private UUID uuid;
    private long timeSinceLastKeepAlive;
    private int ticksAlive;
    private double health;
    private double food;

    public SparkyPlayer(PlayerConnection connection) {
        this.connection = connection;
    }

    public PlayerConnection getConnection() {
        return connection;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public long getTimeSinceLastKeepAlive() {
        return timeSinceLastKeepAlive;
    }

    public void setTimeSinceLastKeepAlive(long timeSinceLastKeepAlive) {
        this.timeSinceLastKeepAlive = timeSinceLastKeepAlive;
    }

    public int getTicksAlive() {
        return ticksAlive;
    }

    public void setTicksAlive(int ticksAlive) {
        this.ticksAlive = ticksAlive;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public double getFood() {
        return food;
    }

    public void setFood(double food) {
        this.food = food;
    }

    public void sendMessage(TextComponent component) {
        connection.sendPacket(new ServerChatMessage(component, ServerChatMessage.MessageType.CHAT));
    }
}
