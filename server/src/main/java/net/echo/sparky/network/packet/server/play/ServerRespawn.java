package net.echo.sparky.network.packet.server.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.enums.Difficulty;
import net.echo.sparkyapi.enums.Dimension;
import net.echo.sparkyapi.enums.GameMode;
import net.echo.sparkyapi.enums.LevelType;

public class ServerRespawn implements Packet.Server {

    private Dimension dimension;
    private Difficulty difficulty;
    private GameMode gameMode;
    private LevelType levelType;

    public ServerRespawn() {
    }

    public ServerRespawn(Dimension dimension, Difficulty difficulty, GameMode gameMode, LevelType levelType) {
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.gameMode = gameMode;
        this.levelType = levelType;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeInt(dimension.id());
        buffer.writeByte(difficulty.id());
        buffer.writeByte(gameMode.id());
        buffer.writeString(levelType.name().toLowerCase());
    }
}
