package net.echo.sparky.tick;

import io.netty.channel.ChannelFuture;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.ConnectionManager;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.player.PlayerConnection.*;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.LockSupport;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

public class TickSchedulerThread extends Thread {

    private final MinecraftServer server;
    private final Queue<Runnable> packetQueue = new ConcurrentLinkedQueue<>();

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
        for (Runnable runnable : packetQueue) {
            runnable.run();
        }

        // TODO: Tick everything

        packetQueue.clear();

        NetworkManager networkManager = server.getNetworkManager();
        ConnectionManager connectionManager = networkManager.getConnectionManager();

        for (PlayerConnection connection : connectionManager.getAll()) {
            var queue = connection.getPacketQueue();
            var entries = queue.entrySet();

            for (Map.Entry<Packet.Server, GenericFutureListener<? extends Future<? super Void>>> entry : entries) {
                ChannelFuture future = connection.getChannel().writeAndFlush(entry.getKey());

                if (entry.getValue() != null) {
                    future.addListener(entry.getValue());
                }

                future.addListener(FIRE_EXCEPTION_ON_FAILURE);
            }

            connection.getPacketQueue().clear();
        }
    }

    public Queue<Runnable> getPacketQueue() {
        return packetQueue;
    }
}
