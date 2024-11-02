package net.echo.sparkyapi.enums.world;

public enum GameMode {

    SURVIVAL, CREATIVE, ADVENTURE, SPECTATOR;

    public int id() {
        return this.ordinal();
    }
}
