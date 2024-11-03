package net.echo.sparky.network.pipeline.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.NetworkManager;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.state.ConnectionState;
import net.echo.sparky.network.state.PacketOwnership;

public class PacketEncoder extends MessageToByteEncoder<Packet.Server> {

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet.Server packet, ByteBuf buffer) {
        NetworkBuffer packetBuffer = new NetworkBuffer(buffer);

        ConnectionState state = ctx.channel().attr(NetworkManager.CONNECTION_STATE).get();
        int packetId = state.getIdFromPacket(PacketOwnership.SERVER, packet);

        packetBuffer.writeVarInt(packetId);
        packet.write(packetBuffer);
    }
}
