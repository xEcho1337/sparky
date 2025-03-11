package net.echo.server;

import java.nio.ByteBuffer;
import java.util.UUID;

public class NetworkBuffer {

    private final ByteBuffer buffer;

    public NetworkBuffer() {
        this.buffer = ByteBuffer.allocate(1024);
    }

    public NetworkBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    public void mark() {
        buffer.mark();
    }

    public void reset() {
        buffer.reset();
    }

    public byte readByte() {
        return buffer.get();
    }

    public void writeByte(int value) {
        buffer.put((byte) value);
    }

    public short readShort() {
        return buffer.getShort();
    }

    public void writeShort(int value) {
        buffer.putShort((short) value);
    }

    public int readUnsignedShort() {
        return Short.toUnsignedInt(buffer.getShort());
    }

    public int readInt() {
        return buffer.getInt();
    }

    public void writeInt(int value) {
        buffer.putInt(value);
    }

    public long readLong() {
        return buffer.getLong();
    }

    public void writeLong(long value) {
        buffer.putLong(value);
    }

    public float readFloat() {
        return buffer.getFloat();
    }

    public void writeFloat(float value) {
        buffer.putFloat(value);
    }

    public double readDouble() {
        return buffer.getDouble();
    }

    public void writeDouble(double value) {
        buffer.putDouble(value);
    }

    public String readString() {
        int length = readVarInt();

        if (length > 32767) {
            throw new IllegalArgumentException("Invalid length! Length must be smaller than 32767, was " + length + "!");
        }

        byte[] bytes = new byte[length];
        buffer.get(bytes);

        return new String(bytes);
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes();

        writeVarInt(bytes.length);
        buffer.put(bytes);
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
            writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }

        writeByte(value & 0x7F);
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

    public void writeBytes(byte[] bytes) {
        buffer.put(bytes);
    }

    public void writeBytes(byte[] bytes, int index, int length) {
        buffer.put(bytes, index, length);
    }

    public int readableBytes() {
        return buffer.remaining();
    }

    public void writeUUID(UUID uniqueId) {
        writeLong(uniqueId.getMostSignificantBits());
        writeLong(uniqueId.getLeastSignificantBits());
    }

    public UUID readUUID() {
        return new UUID(readLong(), readLong());
    }

    public void writeBoolean(boolean value) {
        buffer.put((byte) (value ? 1 : 0));
    }

    public boolean readBoolean() {
        return buffer.get() == 1;
    }

    public byte[] readBytes(int size) {
        byte[] bytes = new byte[size];
        buffer.get(bytes);
        return bytes;
    }
}
