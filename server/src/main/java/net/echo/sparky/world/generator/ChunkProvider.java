package net.echo.sparky.world.generator;

import net.echo.sparky.world.generator.unit.GenerationUnit;

import java.util.function.Consumer;

public class ChunkProvider {

    private Consumer<GenerationUnit> unit;

    public void setGenerator(Consumer<GenerationUnit> unit) {
        this.unit = unit;
    }

    public Consumer<GenerationUnit> getUnit() {
        return unit;
    }
}
