package net.echo.sparky.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.echo.sparky.math.Vector3i;

import java.io.IOException;
import java.util.UUID;

public class NetworkBuffer {

    private final ByteBuf buffer;

    public NetworkBuffer(ByteBuf buffer) {
        this.buffer = buffer;
    }

    public byte readByte() {
        return buffer.readByte();
    }

    public void writeByte(int value) {
        buffer.writeByte(value);
    }

    public short readShort() {
        return buffer.readShort();
    }

    public void writeShort(int value) {
        buffer.writeShort(value);
    }

    public int readUnsignedShort() {
        return buffer.readUnsignedShort();
    }

    public int readInt() {
        return buffer.readInt();
    }

    public void writeInt(int value) {
        buffer.writeInt(value);
    }

    public long readLong() {
        return buffer.readLong();
    }

    public void writeLong(long value) {
        buffer.writeLong(value);
    }

    public float readFloat() {
        return buffer.readFloat();
    }

    public void writeFloat(float value) {
        buffer.writeFloat(value);
    }

    public double readDouble() {
        return buffer.readDouble();
    }

    public void writeDouble(double value) {
        buffer.writeDouble(value);
    }

    public String readString() {
        int length = readVarInt();

        if (length > 32767) {
            throw new IllegalArgumentException("Invalid length! Length must be smaller than 32767, was " + length + "!");
        }

        byte[] bytes = new byte[length];
        buffer.readBytes(bytes);

        return new String(bytes);
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes();

        writeVarInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public int readVarInt() {
        int value = 0;
        int length = 0;
        byte currentByte;

        do {
            currentByte = readByte();
            value |= (currentByte & 0x7F) << (length * 7);
            length++;

            if (length > 5) {
                throw new RuntimeException("VarInt is too large. Must be smaller than 5 bytes.");
            }
        } while ((currentByte & 0x80) == 0x80);
        return value;
    }

    public void writeVarInt(int value) {
        while ((value & 0xFFFFFF80) != 0L) {
            buffer.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }

        buffer.writeByte(value & 0x7F);
    }

    public long readVarLong() {
        long value = 0;
        int size = 0;
        int b;

        while (((b = readByte()) & 0x80) == 0x80) {
            value |= (long) (b & 0x7F) << (size++ * 7);
        }

        return value | ((long) (b & 0x7F) << (size * 7));
    }

    public void writeVarLong(long value) {
        while ((value & ~0x7F) != 0) {
            writeByte((int) (value & 0x7F) | 0x80);
            value >>>= 7;
        }

        writeByte((int) value);
    }

    public void writeBytes(ByteBuf buffer) {
        this.buffer.writeBytes(buffer);
    }

    public void writeBytes(byte[] bytes) {
        buffer.writeBytes(bytes);
    }

    public void release() {
        buffer.release();
    }

    public int readableBytes() {
        return buffer.readableBytes();
    }

    public void writeBytes(ByteBuf buffer, int index, int length) {
        this.buffer.writeBytes(buffer, index, length);
    }

    public void writeUUID(UUID uniqueId) {
        writeLong(uniqueId.getMostSignificantBits());
        writeLong(uniqueId.getLeastSignificantBits());
    }

    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public void writeBoolean(boolean value) {
        buffer.writeBoolean(value);
    }

    public void writePosition(Vector3i position) {
        int x = position.getX();
        int y = position.getY();
        int z = position.getZ();
        buffer.writeLong(((long) (x & 0x3FFFFFF) << 38) | ((long) (z & 0x3FFFFFF) << 12) | (y & 0xFFF));
    }

    public Vector3i readPosition() {
        long value = readLong();
        int x = (int) (value >> 38);
        int y = (int) (value << 52 >> 52);
        int z = (int) (value << 26 >> 38);
        return new Vector3i(x, y, z);
    }

    public boolean readBoolean() {
        return buffer.readBoolean();
    }

    public ByteBuf readBytes(int size) {
        return buffer.readBytes(size);
    }
}
