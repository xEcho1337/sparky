package net.echo.sparky.world.generator.unit;

import net.echo.sparky.world.Block;
import net.echo.sparky.world.World;
import net.echo.sparky.world.chunk.ChunkColumn;
import net.echo.sparky.world.chunk.ChunkSection;

public class GenerationUnit {

    private final World world;

    private static final int CHUNK_SIZE = 16;
    private static final int CHUNK_RANGE = 16;

    public GenerationUnit(World world) {
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
            ChunkSection section = column.getSection(y >> 4);

            fillSection(section, y & 15, block);
        }

        world.addColumn(chunkX, chunkZ, column);
    }

    private void fillSection(ChunkSection section, int y, Block block) {
        for (int x = 0; x < CHUNK_SIZE; x++) {
            for (int z = 0; z < CHUNK_SIZE; z++) {
                section.setBlock(x, y, z, block);
            }
        }
    }
}