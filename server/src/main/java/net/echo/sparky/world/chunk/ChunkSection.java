package net.echo.sparky.world.chunk;

import net.echo.sparky.world.Block;

public class ChunkSection {

    private final char[] blocks = new char[4096];

    public Block getBlock(int x, int y, int z) {
        return Block.fromCombinedId(getAt(x, y, z));
    }

    public void setBlock(int x, int y, int z, Block block) {
        this.blocks[y << 8 | z << 4 | x] = (char) block.getState();
    }

    public void setBlock(int x, int y, int z, short block) {
        this.blocks[y << 8 | z << 4 | x] = (char) (block << 4 | getData(x, y, z));
    }

    public int getData(int x, int y, int z) {
        return getAt(x, y, z) & 15;
    }

    public int getAt(int x, int y, int z) {
        return this.blocks[y << 8 | z << 4 | x];
    }
}
