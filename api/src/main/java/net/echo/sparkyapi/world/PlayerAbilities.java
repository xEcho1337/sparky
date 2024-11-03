package net.echo.sparkyapi.world;

import java.util.Set;

public class PlayerAbilities {

    public static final PlayerAbilities IS_CREATIVE = new PlayerAbilities(1 << 1);
    public static final PlayerAbilities IS_FLYING = new PlayerAbilities(1 << 2);
    public static final PlayerAbilities CAN_FLY = new PlayerAbilities(1 << 3);
    public static final PlayerAbilities GOD_MODE = new PlayerAbilities(1 << 4);

    private final byte mask;

    public PlayerAbilities(int mask) {
        this.mask = (byte) mask;
    }

    public static PlayerAbilities fromFlags(byte flags) {
        return new PlayerAbilities(flags);
    }

    public PlayerAbilities combine(PlayerAbilities relativeFlag) {
        return new PlayerAbilities(this.mask | relativeFlag.mask);
    }

    public byte getMask() {
        return mask;
    }

    public boolean isSet(byte flags) {
        return (flags & mask) != 0;
    }

    public byte set(byte flags, boolean relative) {
        if (relative) {
            return (byte) (flags | mask);
        }
        return (byte) (flags & ~mask);
    }
}
