package net.echo.sparky.tick;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.impl.AsyncPostFlushEvent;
import net.echo.sparky.event.impl.AsyncPreFlushEvent;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.server.play.ServerKeepAlive;
import net.echo.sparky.network.player.ConnectionManager;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.player.SparkyPlayer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

public class TickSchedulerThread extends Thread {

    private final MinecraftServer server;
    private final Queue<Runnable> scheduledTasks = new ConcurrentLinkedQueue<>();

    private int currentTick;

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
        this.currentTick++;

        long start = System.nanoTime();

        for (Runnable runnable : scheduledTasks) {
            runnable.run();
        }

        // TODO: Tick everything
        long now = System.currentTimeMillis();


        for (SparkyPlayer player : server.getPlayerList()) {
            long difference = System.currentTimeMillis() - player.getTimeSinceLastKeepAlive();

            if (difference > 10 * 1000) {
                player.setTimeSinceLastKeepAlive(now);
                player.getConnection().sendPacket(new ServerKeepAlive((int) (now % 10000000)));
            }
        }

        scheduledTasks.clear();

        flush();

        long took = System.nanoTime() - start;

        if (took > 1_000_000) {
            server.getLogger().warn("Took {} ms to tick", took / 1e6);
        }
    }

    private void flush() {
        NetworkManager networkManager = server.getNetworkManager();
        ConnectionManager connectionManager = networkManager.getConnectionManager();

        AsyncPreFlushEvent preFlushEvent = new AsyncPreFlushEvent();

        server.getEventHandler().call(preFlushEvent);

        for (PlayerConnection connection : connectionManager.getAll()) {
            var entries = connection.getPacketQueue().entrySet();

            for (var entry : entries) {
                connection.dispatchOnThread(entry.getKey(), entry.getValue());
            }

            connection.getPacketQueue().clear();
        }

        AsyncPostFlushEvent postFlushEvent = new AsyncPostFlushEvent();

        server.getEventHandler().call(postFlushEvent);
    }

    public Queue<Runnable> getScheduledTasks() {
        return scheduledTasks;
    }
}
