package net.echo.sparky.world.chunk;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

import java.util.Collection;

public class ChunkColumn {

    private final Int2ObjectMap<ChunkSection> sections = new Int2ObjectArrayMap<>();
    private final int x;
    private final int z;

    public ChunkColumn(int x, int z) {
        this.x = x;
        this.z = z;

        for (int y = 0; y < 16; y++) {
            sections.put(y, new ChunkSection());
        }
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public Collection<ChunkSection> getSections() {
        return sections.values();
    }

    public ChunkSection getSection(int index) {
        return sections.get(index);
    }
}
