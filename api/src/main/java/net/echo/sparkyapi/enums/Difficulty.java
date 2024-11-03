package net.echo.sparkyapi.enums;

public enum Difficulty {

    PEACEFUL, EASY, NORMAL, HARD;

    public int id() {
        return this.ordinal();
    }
}