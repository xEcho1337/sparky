package net.echo.sparky.tick;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.impl.async.AsyncPostFlushEvent;
import net.echo.sparky.event.impl.async.AsyncPreFlushEvent;
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
        long ticksPerSecond = server.getConfig().getTickRate();
        long tickDelay = (long) (1e9 / ticksPerSecond);

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
        for (SparkyPlayer player : server.getPlayerList()) {
            player.tick();
        }

        scheduledTasks.clear();

        long flushStart = System.nanoTime();
        flush();
        long flushTook = System.nanoTime() - flushStart;

        if (flushTook > 1_000_000) {
            server.getLogger().warn("Took {} ms to flush", flushTook / 1e6);
        }

        long took = System.nanoTime() - start;

        if (took > 1_000_000) {
            server.getLogger().warn("Took {} ms to tick", took / 1e6);
        }
    }

    private void flush() {
        AsyncPreFlushEvent preFlushEvent = new AsyncPreFlushEvent();

        server.getEventHandler().call(preFlushEvent);

        for (SparkyPlayer player : server.getPlayerList()) {
            player.getConnection().getChannel().flushQueue();
        }

        AsyncPostFlushEvent postFlushEvent = new AsyncPostFlushEvent();

        server.getEventHandler().call(postFlushEvent);
    }

    public Queue<Runnable> getScheduledTasks() {
        return scheduledTasks;
    }

    public int getCurrentTick() {
        return currentTick;
    }
}
