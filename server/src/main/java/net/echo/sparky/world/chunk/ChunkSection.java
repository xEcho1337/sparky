package net.echo.sparky.world.chunk;

import net.echo.sparkyapi.world.Block;
import net.echo.sparkyapi.world.chunk.Section;

public class ChunkSection implements Section {

    private final char[] blocks = new char[4096];
    private int nonAirBlocks;

    @Override
    public Block getBlock(int x, int y, int z) {
        return Block.fromCombinedId(getAt(x, y, z));
    }

    @Override
    public void setBlock(int x, int y, int z, Block block) {
        if (getAt(x, y, z) != 0) {
            this.nonAirBlocks--;
        }

        if (block != Block.AIR) {
            this.nonAirBlocks++;
        }

        this.blocks[y << 8 | z << 4 | x] = (char) block.getState();
    }

    @Override
    public void setBlock(int x, int y, int z, short block) {
        if (getAt(x, y, z) == 0) {
            this.nonAirBlocks--;
        }

        if (block != 0) {
            this.nonAirBlocks++;
        }

        this.blocks[y << 8 | z << 4 | x] = (char) (block << 4 | getData(x, y, z));
    }

    @Override
    public int getData(int x, int y, int z) {
        return getAt(x, y, z) & 15;
    }

    @Override
    public int getAt(int x, int y, int z) {
        return this.blocks[y << 8 | z << 4 | x];
    }

    @Override
    public int getNonAirBlocks() {
        return nonAirBlocks;
    }

    @Override
    public char[] getBlocks() {
        return blocks;
    }
}
