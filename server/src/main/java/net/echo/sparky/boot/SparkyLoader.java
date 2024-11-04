package net.echo.sparky.boot;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.world.generator.ChunkProvider;
import net.echo.sparky.world.generator.unit.impl.FlatWorldGenerator;

public class SparkyLoader {

    public static void main(String[] args) {
        MinecraftServer server = new MinecraftServer();

        ChunkProvider provider = server.getChunkProvider();
        provider.setGenerator(new FlatWorldGenerator(4));

        server.start();
    }
}
