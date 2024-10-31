package net.echo.sparky.tick;

import net.echo.sparky.MinecraftServer;

import java.util.concurrent.locks.LockSupport;

public class TickSchedulerThread extends Thread {

    private MinecraftServer server;

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
                lastTickBalance = (long) (System.nanoTime() - maxCatchupNanos);
            }
        }
    }

    private void tick() {

    }
}
