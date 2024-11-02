package net.echo.sparky.network.pipeline.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.network.state.PacketOwnership;

import java.io.IOException;
import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) throws Exception {
        if (buffer.readableBytes() == 0) return;

        NetworkBuffer packetBuffer = new NetworkBuffer(buffer);

        int id = packetBuffer.readVarInt();

        ConnectionState state = ctx.channel().attr(NetworkManager.CONNECTION_STATE).get();
        Packet.Client packet = (Packet.Client) state.getPacketFromId(PacketOwnership.CLIENT, id);

        if (packet == null) {
            throw new IOException("Unknown packet with id " + id);
        }

        packet.read(packetBuffer);

        if (packetBuffer.readableBytes() > 0) {
            throw new IOException(String.format("Packet %s/%d (%s) was larger than expected, found %d bytes extra whilst reading packet %d",
                    state.name(), id, packet.getClass().getSimpleName(), packetBuffer.readableBytes(), id));
        }

        out.add(packet);
    }
}
