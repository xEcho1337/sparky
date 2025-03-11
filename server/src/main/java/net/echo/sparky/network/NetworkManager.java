package net.echo.sparky.network;

import net.echo.server.TcpServer;
import net.echo.server.attributes.Attribute;
import net.echo.server.bootstrap.Settings;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.Pipeline;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.pipeline.PacketHandler;
import net.echo.sparky.network.pipeline.inbound.MessageSplitter;
import net.echo.sparky.network.pipeline.inbound.PacketDecoder;
import net.echo.sparky.network.pipeline.outbound.MessageSerializer;
import net.echo.sparky.network.pipeline.outbound.PacketEncoder;
import net.echo.sparky.network.player.ConnectionManager;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;

public class NetworkManager {

    public static final Attribute<ConnectionState> CONNECTION_STATE = new Attribute<>("connection_state");

    private final MinecraftServer server;
    private final ConnectionManager connectionManager;

    private TcpServer<PlayerConnection> tcpServer;

    public NetworkManager(MinecraftServer server) {
        this.server = server;
        this.connectionManager = new ConnectionManager();
    }

    public void start(int port) {
        int threads = server.getConfig().getNettyThreads();

        Settings settings = new Settings().setThreads(threads);

        tcpServer = new TcpServer<>(settings) {
            @Override
            public Pipeline<PlayerConnection> getPipeline() {
                return new Pipeline<PlayerConnection>()
                        .append(new MessageSplitter())
                        .append(new PacketDecoder())
                        .append(new PacketEncoder())
                        .append(new MessageSerializer());
            }

            @Override
            public PlayerConnection createConnection(Channel channel) {
                return new PlayerConnection(channel);
            }
        };
        tcpServer.start(port);

        server.getLogger().info("Opened network channel on port {} with {} threads", port, threads);
    }

    public void stop() {
        server.getLogger().info("Closing the network channel");
        tcpServer.stop();
    }

    public MinecraftServer getServer() {
        return server;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
