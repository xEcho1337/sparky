package net.echo.sparky.utils;

import io.netty.channel.ChannelHandlerContext;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.packet.Packet;

public class ThreadScheduleUtils {

    /**
     * Returns true if the packet is getting processed on the main thread, false otherwise.
     */
    public static boolean ensureMainThread(Packet.Client packet, MinecraftServer server, ChannelHandlerContext context) {
        if (Thread.currentThread() == server.getTickSchedulerThread()) return true;

        server.getTickSchedulerThread().getPacketQueue().add(() -> packet.handle(server, context));

        return false;
    }
}
