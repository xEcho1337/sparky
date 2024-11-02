package net.echo.sparky.network.pipeline.outbound;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import net.echo.sparky.network.NetworkBuffer;

public class MessageSerializer extends MessageToByteEncoder<ByteBuf> {

    @Override
    protected void encode(ChannelHandlerContext ctx, ByteBuf buffer, ByteBuf out) {
        int length = buffer.readableBytes();

        NetworkBuffer outBuffer = new NetworkBuffer(out);

        outBuffer.writeVarInt(length);
        outBuffer.writeBytes(buffer, buffer.readerIndex(), length);
    }
}
