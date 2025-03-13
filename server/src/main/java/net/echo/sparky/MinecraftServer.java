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
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Function;

/**
 * The implementation of the Minecraft server.
 */
public class MinecraftServer {

    public static final MinecraftServer INSTANCE = new MinecraftServer();
    public static final Logger LOGGER = LogManager.getLogger(MinecraftServer.class);

    public static final String CONFIG_FILE_NAME = getProperty("config-file", "server.toml", String::valueOf);
    public static final int MAX_CATCHUP_TICKS = getProperty("max-catchup-ticks", "20", Integer::parseInt);

    private final ServerConfig config;
    private final NetworkManager networkManager;
    private final ChunkProvider chunkProvider;
    private final MinecraftEventHandler eventHandler;
    private final TickSchedulerThread tickSchedulerThread;

    private final Queue<SparkyPlayer> playerList = new ConcurrentLinkedQueue<>();
    private final List<SparkyWorld> worldList = new ArrayList<>();

    private volatile boolean running;

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

    public static <T> T getProperty(String name, String defaultValue, Function<String, T> remapper) {
        return remapper.apply(System.getProperty(name, defaultValue));
    }

    public void start() {
        this.running = true;

        new Thread(() -> {
            loadConfiguration();
            initializeServer();
        }, "Main").start();
    }

    private void loadConfiguration() {
        LOGGER.info("Loading configuration file");
        config.load(new File(CONFIG_FILE_NAME));
    }

    private void initializeServer() {
        LOGGER.info("Starting server on port {}", config.getPort());

        networkManager.start(config.getPort());

        if (worldList.isEmpty()) {
            generateDefaultWorld();
        }

        tickSchedulerThread.start();
    }

    private void generateDefaultWorld() {
        LOGGER.info("Generating the default world...");

        SparkyWorld world = new SparkyWorld("world");
        GenerationUnit unit = new GenerationUnit(world);

        chunkProvider.getUnit().accept(unit);
        worldList.add(world);

        LOGGER.info("Generated world '{}'", world.getName());
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

    public Collection<SparkyPlayer> getPlayerList() {
        return playerList;
    }

    public List<SparkyWorld> getWorlds() {
        return worldList;
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