package net.echo.sparkyapi.enums;

public enum GameMode {

    SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR;

    public int id() {
        return this.ordinal();
    }
}
