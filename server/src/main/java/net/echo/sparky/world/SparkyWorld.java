package net.echo.sparky.world;

import it.unimi.dsi.fastutil.longs.Long2ObjectArrayMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import net.echo.sparky.world.chunk.ChunkColumn;
import net.echo.sparky.world.chunk.ChunkSection;
import net.echo.sparkyapi.world.Block;
import net.echo.sparkyapi.world.World;
import net.echo.sparkyapi.world.chunk.Chunk;
import net.echo.sparkyapi.world.chunk.Section;

import java.util.Collection;

public class SparkyWorld implements World {

    private final Long2ObjectMap<Chunk> chunks = new Long2ObjectArrayMap<>();
    private final String name;

    public SparkyWorld(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Chunk getChunkAt(int x, int z) {
        return chunks.get(getChunkIdentifier(x, z));
    }

    @Override
    public long getChunkIdentifier(int x, int z) {
        return ((long) x << 32L) | z & 0xFFFFFFFFL;
    }

    @Override
    public Collection<Chunk> getChunks() {
        return chunks.values();
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        Chunk chunk = getChunkAt(x >> 4, z >> 4);

        if (chunk == null) return null;

        Section section = chunk.getSection(y >> 4);

        if (section == null) return null;

        return section.getBlock(x & 15, y & 15, z & 15);
    }

    @Override
    public void addColumn(int chunkX, int chunkZ, Chunk column) {
        chunks.put(getChunkIdentifier(chunkX, chunkZ), column);
    }
}
