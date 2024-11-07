package net.echo.sparky.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.pipeline.MinecraftPipeline;
import net.echo.sparky.network.player.ConnectionManager;
import net.echo.sparky.network.state.ConnectionState;

public class NetworkManager {

    public static final AttributeKey<ConnectionState> CONNECTION_STATE = AttributeKey.newInstance("connectionState");

    private final MinecraftServer server;
    private final ConnectionManager connectionManager;

    private NioEventLoopGroup bossGroup;
    private NioEventLoopGroup workerGroup;
    private ChannelFuture networkChannel;

    public NetworkManager(MinecraftServer server) {
        this.server = server;
        this.connectionManager = new ConnectionManager();
    }

    public void start(int port) {
        int threads = server.getConfig().getNettyThreads();

        this.bossGroup = new NioEventLoopGroup(threads);
        this.workerGroup = new NioEventLoopGroup(threads);

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

        this.networkChannel = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new MinecraftPipeline(this))
                .bind(port)
                .syncUninterruptibly();

        server.getLogger().info("Opened network channel on port {} with {} threads", port, threads);
    }

    public void stop() {
        server.getLogger().info("Closing the network channel");

        try {
            this.networkChannel.channel().close().sync();
        } catch (InterruptedException e) {
            server.getLogger().error("Failed to close the network channel", e);
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public MinecraftServer getServer() {
        return server;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
