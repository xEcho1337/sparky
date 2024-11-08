package net.echo.sparkyapi.flags.impl;

import net.echo.sparkyapi.flags.RelativeFlag;

public class SkinParts extends RelativeFlag {

    public static final SkinParts CAPE = new SkinParts(0x01);
    public static final SkinParts JACKET = new SkinParts(0x02);
    public static final SkinParts LEFT_SLEEVE = new SkinParts(0x04);
    public static final SkinParts RIGHT_SLEEVE = new SkinParts(0x08);
    public static final SkinParts LEFT_PANTS_LEG = new SkinParts(0x10);
    public static final SkinParts RIGHT_PANTS_LEG = new SkinParts(0x20);
    public static final SkinParts HAT = new SkinParts(0x40);

    public SkinParts(int mask) {
        super(mask);
    }
}
