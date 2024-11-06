package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
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
    public void handle(PacketHandlerProcessor processor) {
        processor.handleSettings(this);
    }

    public enum ChatMode {
        ENABLED, COMMANDS_ONLY, HIDDEN
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public byte getViewDistance() {
        return viewDistance;
    }

    public void setViewDistance(byte viewDistance) {
        this.viewDistance = viewDistance;
    }

    public ChatMode getChatMode() {
        return chatMode;
    }

    public void setChatMode(ChatMode chatMode) {
        this.chatMode = chatMode;
    }

    public boolean isChatColors() {
        return chatColors;
    }

    public void setChatColors(boolean chatColors) {
        this.chatColors = chatColors;
    }

    public byte getSkinParts() {
        return skinParts;
    }

    public void setSkinParts(byte skinParts) {
        this.skinParts = skinParts;
    }
}
