package net.echo.sparkyapi.world.chunk;

import net.echo.sparkyapi.world.Block;

public interface Section {
    Block getBlock(int x, int y, int z);

    void setBlock(int x, int y, int z, Block block);

    void setBlock(int x, int y, int z, short block);

    int getData(int x, int y, int z);

    int getAt(int x, int y, int z);

    int getNonAirBlocks();

    char[] getBlocks();
}
