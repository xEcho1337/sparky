package net.echo.sparky.network.packet.client.login;

import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.event.Listenable;
import net.echo.sparky.event.impl.AsyncLoginStartEvent;
import net.echo.sparky.math.Vector3i;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.login.ServerLoginDisconnect;
import net.echo.sparky.network.packet.server.login.ServerLoginSuccess;
import net.echo.sparky.network.packet.server.play.ServerChunkData;
import net.echo.sparky.network.packet.server.play.ServerJoinGame;
import net.echo.sparky.network.packet.server.play.ServerPositionAndLook;
import net.echo.sparky.network.packet.server.play.ServerSpawnPosition;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.world.World;
import net.echo.sparky.world.chunk.ChunkColumn;
import net.echo.sparkyapi.enums.Difficulty;
import net.echo.sparkyapi.enums.Dimension;
import net.echo.sparkyapi.enums.GameMode;
import net.echo.sparkyapi.enums.LevelType;
import net.echo.sparkyapi.world.RelativeFlag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class ClientLoginStart implements Packet.Client {

    private String name;

    public ClientLoginStart() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
        this.name = buffer.readString();
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {
        Listenable event = new AsyncLoginStartEvent(name);

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        ServerConfig config = server.getConfig();
        Difficulty difficulty = Difficulty.values()[config.getDifficulty()];

        UUID uuid = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));

        connection.setName(name);
        connection.setUuid(uuid);

        server.getLogger().info("{} ({}) logged in.", name, connection.getChannel().remoteAddress());

        World world = server.getWorlds().getFirst();

        if (world == null) {
            TextComponent reason = Component.text("No world found. Contact server administrators.").color(NamedTextColor.RED);
            connection.close(reason);

            return;
        }

        connection.sendPacket(new ServerLoginSuccess(uuid, name),
                future -> connection.getChannel().attr(NetworkManager.CONNECTION_STATE).set(ConnectionState.PLAY));

        connection.sendPacket(new ServerJoinGame(0, GameMode.CREATIVE, Dimension.NETHER, difficulty, config.getMaxPlayers(), LevelType.DEFAULT, false));
        connection.sendPacket(new ServerSpawnPosition(new Vector3i(0, 64, 0)));
        connection.sendPacket(new ServerPositionAndLook(0, 64, 0, 0, 0, RelativeFlag.EMPTY));

        int renderDistance = config.getRenderDistance() / 2;

        for (int x = -renderDistance; x < renderDistance; x++) {
            for (int z = -renderDistance; z < renderDistance; z++) {
                ChunkColumn column = world.getChunkAt(x, z);

                if (column == null) continue;

                connection.sendPacket(new ServerChunkData(column, true));
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
