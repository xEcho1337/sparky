package net.echo.sparkyapi.world.chunk;

import net.echo.sparkyapi.world.Block;

import java.util.Collection;

public interface Chunk {
    int getX();

    int getZ();

    Block getBlock(int x, int y, int z);

    Collection<Section> getSections();

    Section getSection(int index);
}
