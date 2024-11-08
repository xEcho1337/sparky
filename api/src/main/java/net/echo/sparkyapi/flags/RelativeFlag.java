package net.echo.sparkyapi.flags;

public class RelativeFlag {

    private final byte mask;

    public RelativeFlag(int mask) {
        this.mask = (byte) mask;
    }

    public RelativeFlag combine(RelativeFlag flag) {
        return new RelativeFlag(this.mask | flag.mask);
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
