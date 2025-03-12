package net.echo.sparky.network;

import net.echo.server.TcpServer;
import net.echo.server.channel.Channel;
import net.echo.server.pipeline.Pipeline;
import net.echo.server.settings.Settings;
import net.echo.sparky.network.pipeline.PacketHandler;
import net.echo.sparky.network.pipeline.inbound.PacketDecoder;
import net.echo.sparky.network.pipeline.outbound.MessageSerializer;
import net.echo.sparky.network.pipeline.outbound.PacketEncoder;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;

public class TcpServerImpl extends TcpServer<PlayerConnection> {

    public TcpServerImpl(Settings settings) {
        super(settings);
    }

    @Override
    public Pipeline<PlayerConnection> getPipeline() {
        return new Pipeline<PlayerConnection>()
                .addTransmitter(new PacketDecoder())

                .addTransmitter(new PacketEncoder())
                .addTransmitter(new MessageSerializer())

                .addHandler(new PacketHandler());
    }

    @Override
    public PlayerConnection createConnection(Channel channel) {
        channel.setAttribute(NetworkManager.CONNECTION_STATE, ConnectionState.HANDSHAKING);
        return new PlayerConnection(channel);
    }
}
