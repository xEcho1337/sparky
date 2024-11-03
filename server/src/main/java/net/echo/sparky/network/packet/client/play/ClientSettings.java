package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientSettings implements Packet.Client {

    private String locale;
    private byte viewDistance;
    private ChatMode chatMode;
    private boolean chatColors;
    private byte skinParts;

    public ClientSettings() {
    }

    public ClientSettings(String locale, byte viewDistance, ChatMode chatMode, boolean chatColors, byte skinParts) {
        this.locale = locale;
        this.viewDistance = viewDistance;
        this.chatMode = chatMode;
        this.chatColors = chatColors;
        this.skinParts = skinParts;
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.locale = buffer.readString();
        this.viewDistance = buffer.readByte();
        this.chatMode = ChatMode.values()[buffer.readVarInt()];
        this.chatColors = buffer.readBoolean();
        this.skinParts = buffer.readByte();
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {

    }

    public enum ChatMode {
        ENABLED, COMMANDS_ONLY, HIDDEN
    }

    public String getLocale() {
        return locale;
    }

    public byte getViewDistance() {
        return viewDistance;
    }

    public ChatMode getChatMode() {
        return chatMode;
    }

    public boolean isChatColors() {
        return chatColors;
    }

    public byte getSkinParts() {
        return skinParts;
    }
}
