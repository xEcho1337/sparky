package net.echo.sparkyapi.enums;

public enum Dimension {

    NETHER, OVERWORLD, END;

    public int id() {
        return this.ordinal() - 1;
    }
}
