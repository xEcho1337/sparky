package net.echo.sparky.utils;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;

public class ThreadScheduleUtils {

    /**
     * Returns true if the packet is getting processed on the main thread, false otherwise.
     */
    public static boolean ensureMainThread(Packet.Client packet, MinecraftServer server, PlayerConnection connection) {
        if (Thread.currentThread() == server.getTickSchedulerThread()) return true;

        server.getTickSchedulerThread().getScheduledTasks().add(() -> packet.handle(server, connection));

        return false;
    }
}
