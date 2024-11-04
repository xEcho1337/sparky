package net.echo.sparky.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.AttributeKey;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.echo.sparky.MinecraftServer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.pipeline.ChannelPipeline;
import net.echo.sparky.network.pipeline.PacketHandler;
import net.echo.sparky.network.pipeline.inbound.MessageSplitter;
import net.echo.sparky.network.pipeline.inbound.PacketDecoder;
import net.echo.sparky.network.pipeline.outbound.MessageSerializer;
import net.echo.sparky.network.pipeline.outbound.PacketEncoder;
import net.echo.sparky.network.player.ConnectionManager;
import net.echo.sparky.network.state.ConnectionState;

import static io.netty.channel.ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE;

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
                .childHandler(new ChannelPipeline(this))
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
