package net.echo.sparky.network.packet.client.play;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.handler.PacketHandlerProcessor;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.player.GameSettings;

public class ClientSettings implements Packet.Client {

    private GameSettings settings;

    public ClientSettings() {
    }

    public ClientSettings(GameSettings settings) {
        this.settings = settings;
    }

    public ClientSettings(String locale, byte viewDistance, GameSettings.ChatMode chatMode, boolean chatColors, byte skinParts) {
        this(new GameSettings(locale, chatMode, chatColors, viewDistance, skinParts));
    }

    @Override
    public void read(NetworkBuffer buffer) {
        String locale = buffer.readString();
        byte viewDistance = buffer.readByte();
        GameSettings.ChatMode chatMode = GameSettings.ChatMode.values()[buffer.readVarInt()];
        boolean chatColors = buffer.readBoolean();
        byte skinParts = buffer.readByte();

        this.settings = new GameSettings(locale, chatMode, chatColors, viewDistance, skinParts);
    }

    @Override
    public void handle(PacketHandlerProcessor processor) {
        processor.handleSettings(this);
    }

    public GameSettings getSettings() {
        return settings;
    }

    public void setSettings(GameSettings settings) {
        this.settings = settings;
    }
}
