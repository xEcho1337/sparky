package net.echo.sparky.tick;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.ConnectionManager;
import net.echo.sparky.network.player.PlayerConnection;

import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

public class TickSchedulerThread extends Thread {

    private final MinecraftServer server;
    private final Queue<Runnable> scheduledTasks = new ConcurrentLinkedQueue<>();

    public TickSchedulerThread(MinecraftServer server) {
        super("Server-Ticker");
        this.server = server;
    }

    @Override
    public void run() {
        long tickDelay = (long) MinecraftServer.NANOS_BETWEEN_TICKS;
        long lastTickBalance = System.nanoTime();

        double maxCatchupNanos = MinecraftServer.MAX_CATCHUP_TICKS * tickDelay;

        while (server.isRunning()) {
            long timeSinceLastTick = (System.nanoTime() - lastTickBalance);
            long timeSinceNextTick = tickDelay - timeSinceLastTick;

            if (timeSinceNextTick <= 0) {
                tick();
                lastTickBalance += tickDelay;
            }

            if (timeSinceNextTick > 10_000) {
                LockSupport.parkNanos(timeSinceNextTick);
            }

            if (timeSinceLastTick > maxCatchupNanos) {
                server.getLogger().warn("Server is too far behind! {} ms behind", timeSinceLastTick / 1e6);
                lastTickBalance = (long) (System.nanoTime() - maxCatchupNanos);
            }
        }
    }

    private void tick() {
        long start = System.nanoTime();

        for (Runnable runnable : scheduledTasks) {
            runnable.run();
        }

        // TODO: Tick everything

        scheduledTasks.clear();

        NetworkManager networkManager = server.getNetworkManager();
        ConnectionManager connectionManager = networkManager.getConnectionManager();

        for (PlayerConnection connection : connectionManager.getAll()) {
            var entries = connection.getPacketQueue().entrySet();

            for (var entry : entries) {
                connection.dispatchPacket(entry.getKey(), entry.getValue());
            }

            connection.getPacketQueue().clear();
        }

        long took = System.nanoTime() - start;

        // server.getLogger().info("Took " + took / 1e6 + " ms to tick");
    }

    public Queue<Runnable> getScheduledTasks() {
        return scheduledTasks;
    }
}
