package net.echo.sparky.player;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.network.packet.server.play.ServerChatMessage;
import net.echo.sparky.network.packet.server.play.ServerKeepAlive;
import net.echo.sparky.network.packet.server.play.ServerPositionAndLook;
import net.echo.sparky.network.packet.server.play.ServerRespawn;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparkyapi.enums.Difficulty;
import net.echo.sparkyapi.enums.Dimension;
import net.echo.sparkyapi.enums.GameMode;
import net.echo.sparkyapi.enums.LevelType;
import net.echo.sparkyapi.inventory.Inventory;
import net.echo.sparkyapi.player.GameProfile;
import net.echo.sparkyapi.flags.impl.TeleportFlag;
import net.echo.sparkyapi.player.GameSettings;
import net.echo.sparkyapi.world.*;
import net.kyori.adventure.text.TextComponent;

public class SparkyPlayer {

    private final PlayerConnection connection;
    private final Inventory inventory = new Inventory();

    private GameMode gameMode = GameMode.SURVIVAL;
    private Location location = new Location();
    private GameSettings gameSettings;
    private GameProfile gameProfile;
    private World world;
    private String clientBrand;
    private long lastKeepAlive;
    private int ticksAlive = 0;
    private double health = 20;
    private double foodLevel = 5;

    public SparkyPlayer(PlayerConnection connection) {
        this.connection = connection;
    }

    public void sendMessage(TextComponent component) {
        connection.sendPacket(new ServerChatMessage(component, ServerChatMessage.MessageType.CHAT));
    }

    public void teleport(Location location) {
        MinecraftServer.INSTANCE.schedule(() -> {
            if (location.getWorld() != world) {
                ServerConfig config = MinecraftServer.INSTANCE.getConfig();
                Difficulty difficulty = config.getDifficulty();

                ServerRespawn respawn = new ServerRespawn(Dimension.OVERWORLD, difficulty, GameMode.SURVIVAL, LevelType.DEFAULT);

                connection.sendPacket(respawn);
                world = location.getWorld();
            }

            ServerPositionAndLook teleport = new ServerPositionAndLook(location, TeleportFlag.EMPTY);

            connection.sendPacket(teleport);
        });

        this.location = location;
    }

    public void tick() {
        long difference = System.currentTimeMillis() - lastKeepAlive;

        if (difference > 10 * 1000) {
            this.lastKeepAlive = System.currentTimeMillis();

            int id = (int) (lastKeepAlive % 1_000_000);
            this.connection.sendPacket(new ServerKeepAlive(id));
        }
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
        teleport(location);
    }

    public GameSettings getGameSettings() {
        return gameSettings;
    }

    public void setGameSettings(GameSettings gameSettings) {
        if (this.gameSettings != null) {
            throw new IllegalStateException("Game settings already set!");
        }

        this.gameSettings = gameSettings;
    }

    public GameProfile getGameProfile() {
        return gameProfile;
    }

    public void setGameProfile(GameProfile gameProfile) {
        this.gameProfile = gameProfile;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public String getClientBrand() {
        return clientBrand;
    }

    public void setClientBrand(String clientBrand) {
        if (this.clientBrand != null) {
            throw new IllegalStateException("Client brand already set!");
        }

        this.clientBrand = clientBrand;
    }

    public long getLastKeepAlive() {
        return lastKeepAlive;
    }

    public void setLastKeepAlive(long lastKeepAlive) {
        this.lastKeepAlive = lastKeepAlive;
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

    public double getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(double foodLevel) {
        this.foodLevel = foodLevel;
    }
}
