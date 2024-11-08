package net.echo.sparkyapi.flags.impl;

import net.echo.sparkyapi.flags.RelativeFlag;

// Credits to retrooper (packetevents)
public class TeleportFlag extends RelativeFlag {

    public static final TeleportFlag EMPTY = new TeleportFlag(0);
    public static final TeleportFlag X = new TeleportFlag(1);
    public static final TeleportFlag Y = new TeleportFlag(2);
    public static final TeleportFlag Z = new TeleportFlag(4);
    public static final TeleportFlag YAW = new TeleportFlag(8);
    public static final TeleportFlag PITCH = new TeleportFlag(16);

    public TeleportFlag(int mask) {
        super(mask);
    }
}