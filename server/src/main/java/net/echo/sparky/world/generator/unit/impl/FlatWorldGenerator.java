package net.echo.sparky.world.generator.unit.impl;

import net.echo.sparky.world.Block;
import net.echo.sparky.world.generator.unit.GenerationUnit;

import java.util.function.Consumer;

public class FlatWorldGenerator implements Consumer<GenerationUnit> {

    private final int height;

    public FlatWorldGenerator(int height) {
        this.height = height;
    }

    @Override
    public void accept(GenerationUnit unit) {
        unit.fill(0, 1, Block.BEDROCK);
        unit.fill(1, height - 1, Block.DIRT);
        unit.fill(height - 1, height, Block.GRASS);
    }
}
