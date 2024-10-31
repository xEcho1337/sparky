package net.echo.sparky.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.echo.sparky.world.chunk.ChunkColumn;
import net.echo.sparky.world.chunk.ChunkSection;

import java.util.Collection;

public class World {

    private final Long2ObjectMap<ChunkColumn> chunks = new Long2ObjectArrayMap<>();
    private final String name;

    public World(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public ChunkColumn getChunkAt(int x, int z) {
        return chunks.get(getChunkIdentifier(x, z));
    }

    public long getChunkIdentifier(int x, int z) {
        return ((long) x << 32L) | z & 0xFFFFFFFFL;
    }

    public Collection<ChunkColumn> getChunks() {
        return chunks.values();
    }

    public Block getBlock(int x, int y, int z) {
        ChunkColumn chunk = getChunkAt(x >> 4, z >> 4);

        if (chunk == null) return null;

        ChunkSection section = chunk.getSection(y >> 4);

        if (section == null) return null;

        return section.getBlock(x & 15, y & 15, z & 15);
    }

    public void addColumn(int chunkX, int chunkZ, ChunkColumn column) {
        chunks.put(getChunkIdentifier(chunkX, chunkZ), column);
    }
}
