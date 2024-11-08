package net.echo.sparky.world.chunk;

import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.echo.sparkyapi.world.Block;
import net.echo.sparkyapi.world.chunk.Chunk;
import net.echo.sparkyapi.world.chunk.Section;

import java.util.Collection;

public class ChunkColumn implements Chunk {

    private final Int2ObjectMap<Section> sections = new Int2ObjectArrayMap<>();
    private final int x;
    private final int z;

    public ChunkColumn(int x, int z) {
        this.x = x;
        this.z = z;

        for (int y = 0; y < 16; y++) {
            sections.put(y, new ChunkSection());
        }
    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getZ() {
        return z;
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        Section section = getSection(y >> 4);

        if (section != null) {
            return section.getBlock(x, y, z);
        }

        return null;
    }

    @Override
    public Collection<Section> getSections() {
        return sections.values();
    }

    @Override
    public Section getSection(int index) {
        return sections.get(index);
    }
}
