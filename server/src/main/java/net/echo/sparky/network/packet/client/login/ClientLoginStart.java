package net.echo.sparky.network.packet.client.login;

import io.netty.channel.ChannelHandlerContext;
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
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.world.World;
import net.echo.sparky.world.chunk.ChunkColumn;
import net.echo.sparkyapi.world.RelativeFlag;
import net.kyori.adventure.text.Component;
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
    public void handle(MinecraftServer server, ChannelHandlerContext context) {
        Listenable event = new AsyncLoginStartEvent(name);

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        ServerConfig config = server.getConfig();
        net.echo.sparkyapi.enums.world.Difficulty difficulty = net.echo.sparkyapi.enums.world.Difficulty.values()[config.getDifficulty()];

        UUID uuid = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));

        server.getLogger().info("{} ({}) logged in.", name, context.channel().remoteAddress());

        World world = server.getWorlds().getFirst();

        if (world == null) {
            context.channel().writeAndFlush(new ServerLoginDisconnect(
                    Component.text("No world found. Contact server administrators.")
                    .color(NamedTextColor.RED)
            ));
            return;
        }

        context.channel().writeAndFlush(new ServerLoginSuccess(uuid, name));
        context.channel().attr(NetworkManager.CONNECTION_STATE).set(ConnectionState.PLAY);

        context.channel().writeAndFlush(new ServerJoinGame(0, net.echo.sparkyapi.enums.world.GameMode.CREATIVE, net.echo.sparkyapi.enums.world.Dimension.OVERWORLD,
                difficulty, config.getMaxPlayers(), net.echo.sparkyapi.enums.world.LevelType.DEFAULT, false));
        context.channel().writeAndFlush(new ServerSpawnPosition(new Vector3i(0, 64, 0)));
        context.channel().writeAndFlush(new ServerPositionAndLook(0, 64, 0, 0, 0, RelativeFlag.EMPTY));

        ChunkColumn column = world.getChunkAt(0, 0);

        context.channel().writeAndFlush(new ServerChunkData(column, true));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
