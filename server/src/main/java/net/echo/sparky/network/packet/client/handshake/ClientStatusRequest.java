package net.echo.sparky.network.packet.client.handshake;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.config.ServerConfig;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.server.play.ServerStatusResponse;
import net.echo.sparky.network.player.PlayerConnection;

public class ClientStatusRequest implements Packet.Client {

    public ClientStatusRequest() {
    }

    @Override
    public void read(NetworkBuffer buffer) {
    }

    @Override
    public void handle(MinecraftServer server, PlayerConnection connection) {
        ServerConfig config = server.getConfig();

        ServerStatusResponse response = new ServerStatusResponse(
                new ServerStatusResponse.Players(config.getMaxPlayers(), 5),
                new ServerStatusResponse.Version(config.getPingVersionHover(), 47),
                new ServerStatusResponse.Description(config.getMotd())
        );

        connection.getChannel().writeAndFlush(response);
    }
}
