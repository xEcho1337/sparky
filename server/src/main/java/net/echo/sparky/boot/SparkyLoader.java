package net.echo.sparky.boot;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.MinecraftEventHandler;
import net.echo.sparky.event.impl.AsyncLoginStartEvent;
import net.echo.sparky.world.Block;
import net.echo.sparky.world.World;
import net.echo.sparky.world.generator.ChunkProvider;

public class SparkyLoader {

    public static void main(String[] args) {
        MinecraftServer server = new MinecraftServer();

        ChunkProvider provider = server.getChunkProvider();
        provider.setGenerator(unit -> unit.fill(0, 40, Block.GRASS));

        server.start();

        MinecraftEventHandler handler = server.getEventHandler();

        handler.register(AsyncLoginStartEvent.class, (event) -> {
            event.getName();
        });

        World world = server.getWorlds().getFirst();

        System.out.println(world.getBlock(10, 34, 210));
        System.out.println(world.getBlock(10, 34, 1000));
        System.out.println(world.getBlock(10, 41, 210));
    }
}
