package net.echo.sparky.player;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.network.packet.server.play.ServerChatMessage;
import net.echo.sparky.network.packet.server.play.ServerPositionAndLook;
import net.echo.sparky.network.packet.server.play.ServerRespawn;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparkyapi.attribute.Attribute;
import net.echo.sparkyapi.enums.Difficulty;
import net.echo.sparkyapi.enums.Dimension;
import net.echo.sparkyapi.enums.GameMode;
import net.echo.sparkyapi.enums.LevelType;
import net.echo.sparkyapi.world.*;
import net.kyori.adventure.text.TextComponent;

import java.util.UUID;

public class SparkyPlayer {

    private final PlayerConnection connection;
    private final Inventory inventory = new Inventory();

    public final Attribute<String> CLIENT_BRAND = new Attribute<>(null);
    public final Attribute<Integer> TICKS_ALIVE = new Attribute<>(0);
    public final Attribute<Double> HEALTH = new Attribute<>(20d);
    public final Attribute<Double> FOOD = new Attribute<>(5d);

    private GameMode gameMode = GameMode.SURVIVAL;
    private Location location = new Location();
    private GameProfile gameProfile;
    private World world;
    private long timeSinceLastKeepAlive;

    public SparkyPlayer(PlayerConnection connection) {
        this.connection = connection;
    }

    public PlayerConnection getConnection() {
        return connection;
    }

    public Inventory getInventory() {
        return inventory;
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

    public void sendMessage(TextComponent component) {
        connection.sendPacket(new ServerChatMessage(component, ServerChatMessage.MessageType.CHAT));
    }
}
