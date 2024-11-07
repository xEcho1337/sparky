package net.echo.sparky.utils;

import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;

public class ThreadScheduleUtils {

    /**
     * Returns true if the packet is getting processed on the main thread, false otherwise.
     */
    public static boolean ensureMainThread(Packet.Client packet, PacketHandlerProcessor processor) {
        if (Thread.currentThread() == processor.getServer().getTickSchedulerThread()) return true;

        processor.getServer().schedule(() -> packet.handle(processor));

        return false;
    }
}
