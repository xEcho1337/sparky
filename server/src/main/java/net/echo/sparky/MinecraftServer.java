package net.echo.sparky;

import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.event.MinecraftEventHandler;
import net.echo.sparky.tick.TickSchedulerThread;
import net.echo.sparky.world.World;
import net.echo.sparky.world.generator.ChunkProvider;
import net.echo.sparky.world.generator.GenerationUnit;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * The main server instance. Everything starts here.
 */
public class MinecraftServer {

    public static final double TICKS_PER_SECOND = 20.0;
    public static final double NANOS_BETWEEN_TICKS = (long) (1e9 / TICKS_PER_SECOND);
    public static final double MAX_CATCHUP_TICKS = 20;
    public static final String CONFIG_FILE_NAME = "server.toml";

    private final Logger logger = LogManager.getLogger(MinecraftServer.class);
    private final ServerConfig config;
    private final ChunkProvider chunkProvider;
    private final MinecraftEventHandler eventHandler;
    private final List<World> loadedWorlds;
    private boolean running;

    public MinecraftServer() {
        this.config = new ServerConfig();
        this.chunkProvider = new ChunkProvider();
        this.eventHandler = new MinecraftEventHandler();
        this.loadedWorlds = new ArrayList<>();
    }

    public void start() {
        this.running = true;

        loadConfiguration();
        initializeServer();
    }

    private void loadConfiguration() {
        logger.info("Loading configuration file");
        config.load(new File(CONFIG_FILE_NAME));
    }

    private void initializeServer() {
        logger.info("Starting server on port {}", config.getPort());

        if (loadedWorlds.isEmpty()) {
            generateDefaultWorld();
        }

        new TickSchedulerThread(this).start();
    }

    private void generateDefaultWorld() {
        logger.info("No world was found, generating...");

        World world = new World("world");
        GenerationUnit unit = new GenerationUnit(world);

        chunkProvider.getUnit().accept(unit);
        loadedWorlds.add(world);

        logger.info("World '{}' created", world.getName());
    }

    public boolean isRunning() {
        return running;
    }

    public ChunkProvider getChunkProvider() {
        return chunkProvider;
    }

    public MinecraftEventHandler getEventHandler() {
        return eventHandler;
    }

    public List<World> getWorlds() {
        return loadedWorlds;
    }
}