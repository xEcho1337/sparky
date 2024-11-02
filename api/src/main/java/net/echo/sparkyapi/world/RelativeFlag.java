package net.echo.sparkyapi.world;

// Credits to retrooper (packetevents)
public class RelativeFlag {
        
    public static final RelativeFlag X = new RelativeFlag(1);
    public static final RelativeFlag Y = new RelativeFlag(2);
    public static final RelativeFlag Z = new RelativeFlag(4);
    public static final RelativeFlag YAW = new RelativeFlag(8);
    public static final RelativeFlag PITCH = new RelativeFlag(16);
    public static final RelativeFlag EMPTY = new RelativeFlag(0);

    private final byte mask;

    public RelativeFlag(int mask) {
        this.mask = (byte) mask;
    }

    public RelativeFlag combine(RelativeFlag relativeFlag) {
        return new RelativeFlag(this.mask | relativeFlag.mask);
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