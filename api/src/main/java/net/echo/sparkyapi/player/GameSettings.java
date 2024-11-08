package net.echo.sparkyapi.player;

public class GameSettings {

    public enum ChatMode {
        ENABLED, COMMANDS_ONLY, HIDDEN
    }

    /*
    Bit 0 (0x01): Cape enabled
Bit 1 (0x02): Jacket enabled
Bit 2 (0x04): Left Sleeve enabled
Bit 3 (0x08): Right Sleeve enabled
Bit 4 (0x10): Left Pants Leg enabled
Bit 5 (0x20): Right Pants Leg enabled
Bit 6 (0x40): Hat enabled
     */

    private final String locale;
    private final ChatMode chatMode;
    private final boolean chatColors;
    private final int viewDistance;
    private final int skinParts;

    public GameSettings(String locale, ChatMode chatMode, boolean chatColors, int viewDistance, int skinParts) {
        this.locale = locale;
        this.chatMode = chatMode;
        this.chatColors = chatColors;
        this.viewDistance = viewDistance;
        this.skinParts = skinParts;
    }

    public String getLocale() {
        return locale;
    }

    public ChatMode getChatMode() {
        return chatMode;
    }

    public boolean isChatColors() {
        return chatColors;
    }

    public int getViewDistance() {
        return viewDistance;
    }

    public int getSkinParts() {
        return skinParts;
    }
}
