package net.echo.sparky.network.pipeline.inbound;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.server.pipeline.transmitters.Transmitter;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.player.PlayerConnection;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.network.state.PacketOwnership;

import java.io.IOException;
import java.util.Optional;

import static net.echo.sparky.MinecraftServer.LOGGER;

public class PacketDecoder implements Transmitter.In<PlayerConnection, NetworkBuffer, Packet.Client> {

    @Override
    public Packet.Client read(PlayerConnection connection, NetworkBuffer buffer) throws IOException {
        int id = buffer.readVarInt();

        ConnectionState state = connection.getChannel().getAttribute(NetworkManager.CONNECTION_STATE).getValue();
        Optional<Packet> packet = state.getPacketFromId(PacketOwnership.CLIENT, id);

        if (packet.isEmpty()) {
            throw new IOException("Client send invalid packet with id " + id);
        }

        Packet.Client packetClient = (Packet.Client) packet.get();
        packetClient.read(buffer);

        if (buffer.remaining() > 0) {
            throw new IOException(String.format("Packet %s/%d (%s) was larger than expected, found %d bytes extra whilst reading packet %d",
                    state.name(), id, packetClient.getClass().getSimpleName(), buffer.remaining(), id));
        }

        return packetClient;
    }

    @Override
    public void handleException(PlayerConnection connection, Exception exception) {
        LOGGER.error(exception);
        connection.getChannel().close();
    }
}
