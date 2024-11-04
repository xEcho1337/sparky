package net.echo.sparky.network.pipeline;

import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.ChannelConfig;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.pipeline.inbound.MessageSplitter;
import net.echo.sparky.network.pipeline.inbound.PacketDecoder;
import net.echo.sparky.network.pipeline.outbound.MessageSerializer;
import net.echo.sparky.network.pipeline.outbound.PacketEncoder;

public class ChannelPipeline extends ChannelInitializer<SocketChannel> {

    private final NetworkManager networkManager;

    public ChannelPipeline(NetworkManager networkManager) {
        this.networkManager = networkManager;
    }

    @Override
    protected void initChannel(SocketChannel channel) {
        ChannelConfig config = channel.config();

        config.setOption(ChannelOption.TCP_NODELAY, Boolean.TRUE);
        /*config.setOption(ChannelOption.TCP_FASTOPEN, 1);
        config.setOption(ChannelOption.TCP_FASTOPEN_CONNECT, Boolean.TRUE);
        config.setOption(ChannelOption.IP_TOS, 0x18);
        config.setAllocator(ByteBufAllocator.DEFAULT);*/

        io.netty.channel.ChannelPipeline pipeline = channel.pipeline();

        pipeline
                .addLast("timeout", new ReadTimeoutHandler(30))
                .addLast("splitter", new MessageSplitter())
                .addLast("decoder", new PacketDecoder())
                .addLast("serializer", new MessageSerializer())
                .addLast("encoder", new PacketEncoder())
                .addLast("packet_handler", new PacketHandler(networkManager));
    }
}
