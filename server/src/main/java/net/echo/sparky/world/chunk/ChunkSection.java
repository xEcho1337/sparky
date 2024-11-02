package net.echo.sparky.world.chunk;

import net.echo.sparky.world.Block;

public class ChunkSection {

    private final char[] blocks = new char[4096];
    private int nonAirBlocks;

    public Block getBlock(int x, int y, int z) {
        return Block.fromCombinedId(getAt(x, y, z));
    }

    public void setBlock(int x, int y, int z, Block block) {
        if (getAt(x, y, z) != 0) {
            this.nonAirBlocks--;
        }

        if (block != Block.AIR) {
            this.nonAirBlocks++;
        }

        this.blocks[y << 8 | z << 4 | x] = (char) block.getState();
    }

    public void setBlock(int x, int y, int z, short block) {
        if (getAt(x, y, z) == 0) {
            this.nonAirBlocks--;
        }

        if (block != 0) {
            this.nonAirBlocks++;
        }

        this.blocks[y << 8 | z << 4 | x] = (char) (block << 4 | getData(x, y, z));
    }

    public int getData(int x, int y, int z) {
        return getAt(x, y, z) & 15;
    }

    public int getAt(int x, int y, int z) {
        return this.blocks[y << 8 | z << 4 | x];
    }

    public int getNonAirBlocks() {
        return nonAirBlocks;
    }

    public char[] getBlocks() {
        return blocks;
    }
}
