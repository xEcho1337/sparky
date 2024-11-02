package net.echo.sparky.network.pipeline.inbound;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import net.echo.sparky.network.NetworkBuffer;

import java.util.List;

public class MessageSplitter extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf buffer, List<Object> out) {
        // WTF is this Minecraft? I understood half of the code of this shit...
        buffer.markReaderIndex();
        byte[] bytes = new byte[3];

        for (int i = 0; i < bytes.length; ++i) {
            if (!buffer.isReadable()) {
                buffer.resetReaderIndex();
                return;
            }

            bytes[i] = buffer.readByte();

            if (bytes[i] >= 0) {
                NetworkBuffer networkBuffer = new NetworkBuffer(Unpooled.wrappedBuffer(bytes));

                try {
                    int length = networkBuffer.readVarInt();

                    if (buffer.readableBytes() >= length) {
                        out.add(buffer.readBytes(length));
                        return;
                    }

                    buffer.resetReaderIndex();
                } finally {
                    networkBuffer.release();
                }

                return;
            }
        }
    }
}
