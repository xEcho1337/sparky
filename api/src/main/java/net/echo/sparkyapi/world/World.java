package net.echo.sparkyapi.world;

import net.echo.sparkyapi.world.chunk.Chunk;

import java.util.Collection;

public interface World {

    String getName();

    Chunk getChunkAt(int x, int z);

    Collection<Chunk> getChunks();

    Block getBlock(int x, int y, int z);

    void addColumn(int chunkX, int chunkZ, Chunk chunk);

    long getChunkIdentifier(int x, int z);
}
