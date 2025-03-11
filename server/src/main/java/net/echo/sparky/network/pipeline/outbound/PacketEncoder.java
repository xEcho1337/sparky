package net.echo.sparky.network.pipeline.outbound;

import net.echo.server.NetworkBuffer;
import net.echo.server.pipeline.transmitters.Transmitter;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.network.state.PacketOwnership;

import java.io.IOException;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class PacketEncoder implements Transmitter.Out<PlayerConnection, Packet.Server, NetworkBuffer> {

    @Override
    public NetworkBuffer write(PlayerConnection connection, Packet.Server packet) throws IOException {
        NetworkBuffer packetBuffer = new NetworkBuffer();

        ConnectionState state = connection.getChannel().getAttribute(NetworkManager.CONNECTION_STATE).getValue();
        int packetId = state.getIdFromPacket(PacketOwnership.SERVER, packet);

        packetBuffer.writeVarInt(packetId);
        packet.write(packetBuffer);

        return packetBuffer;
    }

    @Override
    public void handleException(PlayerConnection connection, Exception exception) {
        LOGGER.error(exception);
        connection.getChannel().close();
    }
}
