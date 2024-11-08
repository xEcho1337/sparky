package net.echo.sparkyapi.flags.impl;

import net.echo.sparkyapi.flags.RelativeFlag;

public class PlayerAbilities extends RelativeFlag {

    public static final PlayerAbilities IS_CREATIVE = new PlayerAbilities(1 << 1);
    public static final PlayerAbilities IS_FLYING = new PlayerAbilities(1 << 2);
    public static final PlayerAbilities CAN_FLY = new PlayerAbilities(1 << 3);
    public static final PlayerAbilities GOD_MODE = new PlayerAbilities(1 << 4);

    public PlayerAbilities(int mask) {
        super(mask);
    }
}
