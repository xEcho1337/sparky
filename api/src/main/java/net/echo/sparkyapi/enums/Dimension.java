package net.echo.sparkyapi.enums.world;

public enum Dimension {

    NETHER, OVERWORLD, END;

    public int id() {
        return this.ordinal() - 1;
    }
}
