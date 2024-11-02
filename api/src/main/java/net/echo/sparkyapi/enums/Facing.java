package net.echo.sparkyapi.enums;

public enum Facing {

    DOWN(0, 1, -1, "down"),
    UP(1, 0, -1, "up"),
    NORTH(2, 3, 2, "north"),
    SOUTH(3, 2, 0, "south"),
    WEST(4, 5, 1, "west"),
    EAST(5, 4, 3, "east");

    public final int index;
    public final int opposite;
    public final int horizontalIndex;
    public final String name;

    Facing(final int indexIn, final int oppositeIn, final int horizontalIndexIn, final String nameIn) {
        this.index = indexIn;
        this.horizontalIndex = horizontalIndexIn;
        this.opposite = oppositeIn;
        this.name = nameIn;
    }

    public int getIndex() {
        return index;
    }

    public int getOpposite() {
        return opposite;
    }

    public int getHorizontalIndex() {
        return horizontalIndex;
    }

    public String getName() {
        return name;
    }
}
