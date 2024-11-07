package net.echo.sparky.network.handler;

import io.netty.util.Attribute;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.event.impl.async.AsyncChatEvent;
import net.echo.sparky.event.impl.async.AsyncHandshakeEvent;
import net.echo.sparky.event.impl.login.AsyncPreLoginEvent;
import net.echo.sparky.event.impl.login.LoginEvent;
import net.echo.sparky.math.Vector3i;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.client.handshake.ClientHandshake;
import net.echo.sparky.network.packet.client.handshake.ClientPing;
import net.echo.sparky.network.packet.client.handshake.ClientStatusRequest;
import net.echo.sparky.network.packet.client.login.ClientLoginStart;
import net.echo.sparky.network.packet.client.play.*;
import net.echo.sparky.network.packet.server.handshake.ServerPong;
import net.echo.sparky.network.packet.server.login.ServerLoginSuccess;
import net.echo.sparky.network.packet.server.play.*;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.player.SparkyPlayer;
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

public record PacketHandlerProcessor(MinecraftServer server, PlayerConnection connection) {

    public void handleHandshake(ClientHandshake packet) {
        AsyncHandshakeEvent event = new AsyncHandshakeEvent(
                packet.getAddress(),
                packet.getPort(),
                packet.getProtocolVersion(),
                packet.getNextState()
        );

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        ConnectionState nextConnectionState = switch (event.getState()) {
            case LOGIN -> ConnectionState.LOGIN;
            case STATUS -> ConnectionState.STATUS;
        };

        connection.getChannel().attr(NetworkManager.CONNECTION_STATE).set(nextConnectionState);
    }

    public void handlePing(ClientPing packet) {
        connection.flushPacket(new ServerPong(packet.getId()));
    }

    public void handleStatusRequest(ClientStatusRequest packet) {
        ServerConfig config = server.getConfig();

        ServerStatusResponse response = new ServerStatusResponse(
                new ServerStatusResponse.Players(config.getMaxPlayers(), 5),
                new ServerStatusResponse.Version(config.getPingVersionHover(), 47),
                new ServerStatusResponse.Description(config.getMotd())
        );

        connection.getChannel().writeAndFlush(response);
    }

    public void handleLoginStart(ClientLoginStart packet) {
        UUID uuid = UUID.nameUUIDFromBytes(packet.getName().getBytes(StandardCharsets.UTF_8));
        AsyncPreLoginEvent event = new AsyncPreLoginEvent(packet.getName(), uuid);

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        SparkyPlayer player = connection.getPlayer();

        player.setName(event.getName());
        player.setUuid(event.getUuid());

        server.getLogger().info("{} ({}) logged in", event.getName(), connection.getChannel().remoteAddress());

        World world = server.getWorlds().getFirst();

        if (world == null) {
            TextComponent reason = Component.text("No world found. Contact server administrators.").color(NamedTextColor.RED);
            connection.close(reason);

            return;
        }

        Attribute<ConnectionState> stateAttribute = connection.getChannel().attr(NetworkManager.CONNECTION_STATE);

        connection.flushPacket(new ServerLoginSuccess(event.getUuid(), event.getName()));
        stateAttribute.set(ConnectionState.PLAY);

        ServerConfig config = server.getConfig();

        Difficulty difficulty = Difficulty.values()[config.getDifficulty()];

        connection.flushPacket(new ServerJoinGame(0, GameMode.CREATIVE, Dimension.NETHER, difficulty, config.getMaxPlayers(), LevelType.DEFAULT, false));
        connection.flushPacket(new ServerSpawnPosition(new Vector3i(0, 64, 0)));
        connection.flushPacket(new ServerPositionAndLook(0, 64, 0, 0, 0, RelativeFlag.EMPTY));

        int renderDistance = config.getRenderDistance() / 2;

        for (int x = -renderDistance; x < renderDistance; x++) {
            for (int z = -renderDistance; z < renderDistance; z++) {
                ChunkColumn column = world.getChunkAt(x, z);

                if (column == null) continue;

                connection.flushPacket(new ServerChunkData(column, true));
            }
        }

        server.schedule(() -> {
            server.getPlayerList().add(player);

            LoginEvent.LoginResult result = new LoginEvent.LoginResult(LoginEvent.LoginResultType.ALLOWED, "");
            LoginEvent loginEvent = new LoginEvent(player, result);

            server.getEventHandler().call(loginEvent);

            if (loginEvent.getResult().getType() == LoginEvent.LoginResultType.ALLOWED) return;

            connection.close(Component.text(loginEvent.getResult().getReason()).color(NamedTextColor.RED));
        });
    }

    public void handleKeepAlive(ClientKeepAlive packet) {

    }

    public void handleChatMessage(ClientChatMessage packet) {
        AsyncChatEvent event = new AsyncChatEvent(packet.getMessage(), "<%s> %s");

        server.getEventHandler().call(event);

        if (event.isCancelled()) return;

        SparkyPlayer player = connection.getPlayer();

        TextComponent component = Component.text(String.format(event.getFormat(), player.getName(), event.getMessage()));

        server.schedule(() -> server.broadcast(component));
    }

    public void handleUseEntity(ClientUseEntity packet) {

    }

    public void handleIdle(ClientPlayerIdle packet) {

    }

    public void handlePosition(ClientPlayerPosition packet) {

    }

    public void handleLook(ClientPlayerLook packet) {

    }

    public void handlePositionAndLook(ClientPlayerPositionAndLook packet) {

    }

    public void handlePlayerDigging(ClientPlayerDigging packet) {

    }

    public void handleBlockPlacement(ClientBlockPlacement packet) {

    }

    public void handleHeldItemChange(ClientHeldItemChange packet) {

    }

    public void handleArmSwing(ClientArmSwing packet) {

    }

    public void handleEntityAction(ClientEntityAction packet) {

    }

    public void handlePlayerAbilities(ClientPlayerAbilities packet) {

    }

    public void handleSettings(ClientSettings packet) {

    }

    public void handleStatus(ClientStatus packet) {

    }

    public void handlePluginMessage(ClientPluginMessage packet) {

    }
}
