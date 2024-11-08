package net.echo.sparky.world.generator.unit;

import net.echo.sparkyapi.world.Block;
import net.echo.sparky.world.SparkyWorld;
import net.echo.sparky.world.chunk.ChunkColumn;
import net.echo.sparky.world.chunk.ChunkSection;
import net.echo.sparkyapi.world.chunk.Section;

public class GenerationUnit {

    private final SparkyWorld world;

    private static final int CHUNK_SIZE = 16;
    private static final int CHUNK_RANGE = 16;

    public GenerationUnit(SparkyWorld world) {
        this.world = world;
    }

    public void fill(Block block) {
        fill(0, 256, block);
    }

    public void fill(int minHeight, int maxHeight, Block block) {
        for (int chunkX = -CHUNK_RANGE; chunkX < CHUNK_RANGE; chunkX++) {
            for (int chunkZ = -CHUNK_RANGE; chunkZ < CHUNK_RANGE; chunkZ++) {
                fillChunk(chunkX, chunkZ, minHeight, maxHeight, block);
            }
        }
    }

    private void fillChunk(int chunkX, int chunkZ, int minHeight, int maxHeight, Block block) {
        ChunkColumn column = world.getChunkAt(chunkX, chunkZ);

        if (column == null) {
            column = new ChunkColumn(chunkX, chunkZ);
        }

        for (int y = minHeight; y < maxHeight; y++) {
            Section section = column.getSection(y >> 4);

            fillSection(section, y & 15, block);
        }

        world.addColumn(chunkX, chunkZ, column);
    }

    private void fillSection(Section section, int y, Block block) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                section.setBlock(x, y, z, block);
            }
        }
    }
}