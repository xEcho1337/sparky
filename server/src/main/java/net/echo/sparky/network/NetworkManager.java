package net.echo.sparky.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.pipeline.PacketHandler;
import net.echo.sparky.network.pipeline.inbound.MessageSplitter;
import net.echo.sparky.network.pipeline.inbound.PacketDecoder;
import net.echo.sparky.network.pipeline.outbound.MessageSerializer;
import net.echo.sparky.network.pipeline.outbound.PacketEncoder;
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

        this.bossGroup = new NioEventLoopGroup();
        this.workerGroup = new NioEventLoopGroup(threads);

        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.PARANOID);

        this.networkChannel = new ServerBootstrap()
                .group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(getPipeline())
                .bind(port)
                .syncUninterruptibly();

        server.getLogger().info("Created the network channel at port {}", port);
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

    private ChannelInitializer<SocketChannel> getPipeline() {
        return new ChannelInitializer<>() {
            @Override
            protected void initChannel(SocketChannel channel) {
                ChannelPipeline pipeline = channel.pipeline();

                channel.config().setOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);

                pipeline
                        .addLast("timeout", new ReadTimeoutHandler(30))
                        .addLast("splitter", new MessageSplitter())
                        .addLast("decoder", new PacketDecoder())
                        .addLast("serializer", new MessageSerializer())
                        .addLast("encoder", new PacketEncoder())
                        .addLast("packet_handler", new PacketHandler(NetworkManager.this));
            }
        };
    }

    public MinecraftServer getServer() {
        return server;
    }

    public ConnectionManager getConnectionManager() {
        return connectionManager;
    }
}
