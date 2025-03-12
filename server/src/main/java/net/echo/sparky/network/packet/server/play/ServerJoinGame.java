package net.echo.sparky.network.packet.server.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.enums.Difficulty;
import net.echo.sparkyapi.enums.Dimension;
import net.echo.sparkyapi.enums.GameMode;
import net.echo.sparkyapi.enums.LevelType;

public class ServerJoinGame implements Packet.Server {

    private int entityId;
    private GameMode gameMode;
    private Dimension dimension;
    private Difficulty difficulty;
    private int maxPlayers;
    private LevelType levelType;
    private boolean reducedDebugInfo;

    public ServerJoinGame() {
    }

    public ServerJoinGame(int entityId, GameMode gameMode, Dimension dimension, Difficulty difficulty, int maxPlayers, LevelType levelType, boolean reducedDebugInfo) {
        this.entityId = entityId;
        this.gameMode = gameMode;
        this.dimension = dimension;
        this.difficulty = difficulty;
        this.maxPlayers = maxPlayers;
        this.levelType = levelType;
        this.reducedDebugInfo = reducedDebugInfo;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeInt(entityId);
        buffer.writeByte(gameMode.id());
        buffer.writeByte(dimension.id());
        buffer.writeByte(difficulty.id());
        buffer.writeByte(maxPlayers);
        buffer.writeString(levelType.name().toLowerCase());
        buffer.writeBoolean(reducedDebugInfo);
    }
}
