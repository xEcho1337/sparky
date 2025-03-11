package net.echo.sparky;

import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.event.MinecraftEventHandler;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.player.SparkyPlayer;
import net.echo.sparky.tick.TickSchedulerThread;
import net.echo.sparky.world.SparkyWorld;
import net.echo.sparky.world.generator.ChunkProvider;
import net.echo.sparky.world.generator.unit.GenerationUnit;
import net.kyori.adventure.text.TextComponent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The main server instance. Everything starts here.
 */
public class MinecraftServer {

    public static final MinecraftServer INSTANCE = new MinecraftServer();
    public static final double TICKS_PER_SECOND = 20.0;
    public static final double NANOS_BETWEEN_TICKS = (long) (1e9 / TICKS_PER_SECOND);
    public static final double MAX_CATCHUP_TICKS = 20;
    public static final String CONFIG_FILE_NAME = "server.toml";
    public static final Logger LOGGER = LogManager.getLogger(MinecraftServer.class);

    private final ServerConfig config;
    private final NetworkManager networkManager;
    private final ChunkProvider chunkProvider;
    private final MinecraftEventHandler eventHandler;
    private final TickSchedulerThread tickSchedulerThread;

    private final List<SparkyPlayer> playerList = new ArrayList<>();
    private final List<SparkyWorld> loadedWorlds = new ArrayList<>();

    private boolean running;

    public MinecraftServer() {
        if (INSTANCE != null) {
            throw new IllegalStateException("An instance of MinecraftServer already exists");
        }

        this.config = new ServerConfig();
        this.networkManager = new NetworkManager(this);
        this.chunkProvider = new ChunkProvider();
        this.eventHandler = new MinecraftEventHandler();
        this.tickSchedulerThread = new TickSchedulerThread(this);
    }

    public void start() {
        this.running = true;

        loadConfiguration();
        initializeServer();
    }

    private void loadConfiguration() {
        LOGGER.info("Loading configuration file");
        config.load(new File(CONFIG_FILE_NAME));
    }

    private void initializeServer() {
        LOGGER.info("Starting server on port {}", config.getPort());

        networkManager.start(config.getPort());

        if (loadedWorlds.isEmpty()) {
            generateDefaultWorld();
        }

        tickSchedulerThread.start();
    }

    private void generateDefaultWorld() {
        LOGGER.info("No world was found, generating...");

        SparkyWorld world = new SparkyWorld("world");
        GenerationUnit unit = new GenerationUnit(world);

        chunkProvider.getUnit().accept(unit);
        loadedWorlds.add(world);

        LOGGER.info("World '{}' created", world.getName());
    }

    public static MinecraftServer getInstance() {
        return INSTANCE;
    }

    public boolean isRunning() {
        return running;
    }

    public Logger getLogger() {
        return LOGGER;
    }

    public ServerConfig getConfig() {
        return config;
    }

    public NetworkManager getNetworkManager() {
        return networkManager;
    }

    public ChunkProvider getChunkProvider() {
        return chunkProvider;
    }

    public MinecraftEventHandler getEventHandler() {
        return eventHandler;
    }

    public TickSchedulerThread getTickSchedulerThread() {
        return tickSchedulerThread;
    }

    public List<SparkyPlayer> getPlayerList() {
        return playerList;
    }

    public List<SparkyWorld> getWorlds() {
        return loadedWorlds;
    }

    public void broadcast(TextComponent message) {
        for (SparkyPlayer player : playerList) {
            player.sendMessage(message);
        }
    }

    public void schedule(Runnable runnable) {
        tickSchedulerThread.getScheduledTasks().add(runnable);
    }
}