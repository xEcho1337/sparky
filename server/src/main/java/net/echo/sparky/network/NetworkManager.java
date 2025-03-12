package net.echo.sparky.network;

import net.echo.server.TcpServer;
import net.echo.server.attributes.Attribute;
import net.echo.server.settings.Settings;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;

public class NetworkManager {

    public static final Attribute<ConnectionState> CONNECTION_STATE = Attribute.of("connection_state");

    private final MinecraftServer server;
    private TcpServer<PlayerConnection> tcpServer;

    public NetworkManager(MinecraftServer server) {
        this.server = server;
    }

    public void start(int port) {
        int threads = server.getConfig().getThreads();
        Settings settings = new Settings().setThreads(threads);

        tcpServer = new TcpServerImpl(settings);
        tcpServer.start(port);

        server.getLogger().info("Opened network channel on port {} with {} threads", port,
                tcpServer.getSettings().threads());
    }

    public void stop() {
        server.getLogger().info("Closing the network channel");
        tcpServer.stop();
    }

    public MinecraftServer getServer() {
        return server;
    }
}
