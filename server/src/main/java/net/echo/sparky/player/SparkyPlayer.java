package net.echo.sparky.player;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.network.packet.server.play.ServerChatMessage;
import net.echo.sparky.network.packet.server.play.ServerPositionAndLook;
import net.echo.sparky.network.packet.server.play.ServerRespawn;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparkyapi.enums.Difficulty;
import net.echo.sparkyapi.enums.Dimension;
import net.echo.sparkyapi.enums.GameMode;
import net.echo.sparkyapi.enums.LevelType;
import net.echo.sparkyapi.world.GameProfile;
import net.echo.sparkyapi.world.Location;
import net.echo.sparkyapi.world.RelativeFlag;
import net.echo.sparkyapi.world.World;
import net.kyori.adventure.text.TextComponent;

import java.util.UUID;

public class SparkyPlayer {

    private final PlayerConnection connection;

    private GameMode gameMode = GameMode.SURVIVAL;
    private Location location = new Location();
    private World world;
    private GameProfile gameProfile;
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

    public GameMode getGameMode() {
        return gameMode;
    }

    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public World getWorld() {
        return world;
    }

    public void teleport(Location location) {
        if (location.getWorld() != world) {
            ServerConfig config = MinecraftServer.getInstance().getConfig();
            Difficulty difficulty = Difficulty.values()[config.getDifficulty()];

            ServerRespawn respawn = new ServerRespawn(
                    Dimension.OVERWORLD,
                    difficulty,
                    GameMode.SURVIVAL,
                    LevelType.DEFAULT
            );

            connection.sendPacket(respawn);
            world = location.getWorld();
        }

        ServerPositionAndLook teleport = new ServerPositionAndLook(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch(),
                RelativeFlag.EMPTY
        );

        connection.sendPacket(teleport);

        this.location = location;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
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
