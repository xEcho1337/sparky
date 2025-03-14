package net.echo.sparky.network.handler;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.echo.server.buffer.NetworkBuffer;
import net.echo.server.channel.Channel;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.event.impl.async.AsyncChatEvent;
import net.echo.sparky.event.impl.async.AsyncHandshakeEvent;
import net.echo.sparky.event.impl.login.AsyncPreLoginEvent;
import net.echo.sparky.event.impl.login.LoginEvent;
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
import net.echo.sparky.utils.ThreadScheduleUtils;
import net.echo.sparky.world.SparkyWorld;
import net.echo.sparkyapi.enums.Difficulty;
import net.echo.sparkyapi.enums.Dimension;
import net.echo.sparkyapi.enums.GameMode;
import net.echo.sparkyapi.enums.LevelType;
import net.echo.sparkyapi.math.Vector3i;
import net.echo.sparkyapi.player.GameProfile;
import net.echo.sparkyapi.world.Location;
import net.echo.sparkyapi.world.chunk.Chunk;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public record PacketProcessor(MinecraftServer server, PlayerConnection connection) {

    private static final ExecutorService LOGIN_THREAD = Executors.newFixedThreadPool(2,
            getThreadFactory("Async Login Thread - #%d"));
    private static final ExecutorService CHAT_THREAD = Executors.newFixedThreadPool(2,
            getThreadFactory("Async Chat Thread - #%d"));

    private static ThreadFactory getThreadFactory(String name) {
        return new ThreadFactoryBuilder()
                .setDaemon(true)
                .setNameFormat(name)
                .build();
    }

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

        connection.getChannel().setAttribute(NetworkManager.CONNECTION_STATE, nextConnectionState);
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

        connection.flushPacket(response);
    }

    public void handleLoginStart(ClientLoginStart packet) {
        LOGIN_THREAD.submit(() -> {
            Channel channel = connection.getChannel();
            ServerConfig config = server.getConfig();

            String name = packet.getName();
            UUID uuid = UUID.nameUUIDFromBytes(name.getBytes(StandardCharsets.UTF_8));

            AsyncPreLoginEvent event = new AsyncPreLoginEvent(name, uuid);
            server.getEventHandler().call(event);

            if (event.isCancelled()) {
                connection.close(null);
                return;
            }

            if (!name.matches(config.getUsernameFormat())) {
                connection.close(Component.text("Invalid username!").color(NamedTextColor.RED));
                return;
            }

            SparkyPlayer player = connection.getPlayer();
            GameProfile profile = new GameProfile(event.getName(), event.getUuid());

            player.setGameProfile(profile);

            server.getLogger().info("{} ({}) logged in", event.getName(), connection.getChannel().remoteAddress());

            SparkyWorld world = server.getWorlds().getFirst();

            if (world == null) {
                TextComponent reason = Component.text("Missing a default world!");
                connection.close(reason.color(NamedTextColor.RED));
                return;
            }

            connection.flushPacket(new ServerLoginSuccess(event.getUuid(), event.getName()));

            channel.setAttribute(NetworkManager.CONNECTION_STATE, ConnectionState.PLAY);

            Difficulty difficulty = config.getDifficulty();

            Location location = new Location(world, -64, 64, 0, 0, 0);

            connection.sendPacket(new ServerJoinGame(0, GameMode.CREATIVE, Dimension.OVERWORLD, difficulty, config.getMaxPlayers(), LevelType.DEFAULT, false));
            connection.sendPacket(new ServerSpawnPosition(new Vector3i(-64, 0, 0)));
            player.teleport(location);

            server.getPlayerList().add(player);

            LoginEvent.LoginResult result = new LoginEvent.LoginResult(LoginEvent.LoginResultType.ALLOWED, "");
            LoginEvent loginEvent = new LoginEvent(player, result);

            server.getEventHandler().call(loginEvent);

            if (loginEvent.getResult().getType() != LoginEvent.LoginResultType.ALLOWED) {
                TextComponent reason = Component.text(loginEvent.getResult().getReason());
                connection.close(reason.color(NamedTextColor.RED));

                server.getPlayerList().remove(player);
                return;
            }

            List<Chunk> chunks = new ArrayList<>();

            int renderDistance = config.getRenderDistance() / 2;
            for (int x = -renderDistance; x < renderDistance; x++) {
                for (int z = -renderDistance; z < renderDistance; z++) {
                    Chunk column = world.getChunkAt(x, z);

                    if (column == null) continue;

                    chunks.add(column);
                }
            }

            connection.sendPacket(new ServerChunkDataBulk(chunks));
        });
    }

    public void handleKeepAlive(ClientKeepAlive packet) {
    }

    public void handleChatMessage(ClientChatMessage packet) {
        CHAT_THREAD.submit(() -> {
            ServerConfig config = server.getConfig();
            System.out.println("RECEIVED: " + packet.getMessage());
            AsyncChatEvent event = new AsyncChatEvent(packet.getMessage(), config.getChatFormat());

            server.getEventHandler().call(event);

            if (event.isCancelled()) return;

            SparkyPlayer player = connection.getPlayer();
            GameProfile profile = player.getGameProfile();

            TextComponent component = Component.text(String.format(event.getFormat(), profile.getName(), event.getMessage()));
            server.broadcast(component);
        });
    }

    public void handleUseEntity(ClientUseEntity packet) {

    }

    public void handleIdle(ClientPlayerIdle packet) {

    }

    public void handlePosition(ClientPlayerPosition packet) {
        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;

        SparkyPlayer player = connection.getPlayer();
        Location location = player.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        double newX = packet.getX();
        double newY = packet.getY();
        double newZ = packet.getZ();

        if (Double.isInfinite(newX) || Double.isNaN(newX)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        if (Double.isInfinite(newY) || Double.isNaN(newY)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        if (Double.isInfinite(newZ) || Double.isNaN(newZ)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        double deltaX = Math.abs(newX - x);
        double deltaY = Math.abs(newY - y);
        double deltaZ = Math.abs(newZ - z);

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        // TODO: Check for server added velocity
        if (distance > 5) {
            player.teleport(new Location(location.getWorld(), x, y, z, location.getYaw(), location.getPitch()));
        } else {
            location.setX(newX);
            location.setY(newY);
            location.setZ(newZ);
        }
    }

    public void handleLook(ClientPlayerLook packet) {
        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;

        SparkyPlayer player = connection.getPlayer();
        Location location = player.getLocation();

        float yaw = packet.getYaw();
        float pitch = packet.getPitch();

        if (Double.isInfinite(yaw) || Double.isNaN(yaw)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        if (Double.isInfinite(pitch) || Double.isNaN(pitch)) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        location.setYaw(yaw);
        location.setPitch(pitch);
    }

    public void handlePositionAndLook(ClientPlayerPositionAndLook packet) {
        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;

        SparkyPlayer player = connection.getPlayer();
        Location location = player.getLocation();

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();

        double newX = packet.getX();
        double newY = packet.getY();
        double newZ = packet.getZ();

        float yaw = packet.getYaw();
        float pitch = packet.getPitch();

        boolean invalidX = Double.isInfinite(newX) || Double.isNaN(newX);
        boolean invalidY = Double.isInfinite(newY) || Double.isNaN(newY);
        boolean invalidZ = Double.isInfinite(newZ) || Double.isNaN(newZ);
        boolean invalidYaw = Double.isInfinite(yaw) || Double.isNaN(yaw);
        boolean invalidPitch = Double.isInfinite(pitch) || Double.isNaN(pitch);

        if (invalidX || invalidY || invalidZ || invalidYaw || invalidPitch) {
            connection.close(Component.text("Invalid position.").color(NamedTextColor.RED));
            return;
        }

        double deltaX = Math.abs(newX - x);
        double deltaY = Math.abs(newY - y);
        double deltaZ = Math.abs(newZ - z);

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

        // TODO: Check for server added velocity
        if (distance > 5) {
            player.teleport(new Location(location.getWorld(), x, y, z, yaw, pitch));
        } else {
            location.setX(newX);
            location.setY(newY);
            location.setZ(newZ);
            location.setYaw(yaw);
            location.setPitch(pitch);
        }
    }

    public void handlePlayerDigging(ClientPlayerDigging packet) {

    }

    public void handleBlockPlacement(ClientBlockPlacement packet) {

    }

    public void handleHeldItemChange(ClientHeldItemChange packet) {
        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;

        SparkyPlayer player = connection.getPlayer();

        if (packet.getSlot() > 8 || packet.getSlot() < 0) {
            connection.close(Component.text("Invalid slot.").color(NamedTextColor.RED));
            return;
        }

        player.getInventory().setHeldItemSlot(packet.getSlot());
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
        if (!ThreadScheduleUtils.ensureMainThread(packet, this)) return;

        SparkyPlayer player = connection.getPlayer();

        if (packet.getChannel().equals("MC|Brand")) {
            NetworkBuffer data = packet.getData();
            String brand = data.readString();

            player.setClientBrand(brand);
        }
    }
}
