package net.echo.server.buffer;

import net.echo.sparkyapi.math.Vector3i;
import java.nio.ByteBuffer;
import java.util.UUID;

public class NetworkBuffer {

    private ByteBuffer buffer;

    public NetworkBuffer() {
        this.buffer = ByteBuffer.allocate(1024);
    }

    public NetworkBuffer(ByteBuffer buffer) {
        this.buffer = buffer;
    }

    public NetworkBuffer(int size) {
        this.buffer = ByteBuffer.allocate(size);
    }

    public ByteBuffer getBuffer() {
        return buffer;
    }

    private void ensureCapacity(int additionalCapacity) {
        if (buffer.position() + additionalCapacity > buffer.capacity()) {
            int newCapacity = Math.max(buffer.capacity() * 2, buffer.capacity() + additionalCapacity);

            ByteBuffer newBuffer = ByteBuffer.allocate(newCapacity);
            buffer.flip();

            newBuffer.put(buffer);
            buffer = newBuffer;
        }
    }

    public void mark() {
        buffer.mark();
    }

    public void reset() {
        if (buffer.position() > 0) {
            buffer.reset();
        }
    }

    public byte readByte() {
        if (buffer.remaining() < 1) return 0;
        return buffer.get();
    }

    public void writeByte(int value) {
        ensureCapacity(1);
        buffer.put((byte) value);
    }

    public short readShort() {
        return buffer.getShort();
    }

    public void writeShort(int value) {
        ensureCapacity(2);
        buffer.putShort((short) value);
    }

    public int readUnsignedShort() {
        return Short.toUnsignedInt(buffer.getShort());
    }

    public int readInt() {
        return buffer.getInt();
    }

    public void writeInt(int value) {
        ensureCapacity(4);
        buffer.putInt(value);
    }

    public long readLong() {
        return buffer.getLong();
    }

    public void writeLong(long value) {
        ensureCapacity(8);
        buffer.putLong(value);
    }

    public float readFloat() {
        return buffer.getFloat();
    }

    public void writeFloat(float value) {
        ensureCapacity(4);
        buffer.putFloat(value);
    }

    public double readDouble() {
        return buffer.getDouble();
    }

    public void writeDouble(double value) {
        ensureCapacity(8);
        buffer.putDouble(value);
    }

    public String readString() {
        int length = readVarInt();

        if (length > 32767) {
            throw new IllegalArgumentException("Invalid length! Length must be smaller than 32767, was " + length + "!");
        }

        byte[] bytes = new byte[length];
        buffer.get(bytes, 0, length);

        return new String(bytes);
    }

    public void writeString(String value) {
        byte[] bytes = value.getBytes();
        writeVarInt(bytes.length);
        ensureCapacity(bytes.length);
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
        ensureCapacity(5);
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
        ensureCapacity(10);
        while ((value & ~0x7F) != 0) {
            writeByte((int) (value & 0x7F) | 0x80);
            value >>>= 7;
        }
        writeByte((int) value);
    }

    public void writeBytes(byte[] bytes) {
        ensureCapacity(bytes.length);
        buffer.put(bytes);
    }

    public void writeBytes(byte[] bytes, int index, int length) {
        ensureCapacity(length);
        buffer.put(bytes, index, length);
    }

    public int remaining() {
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
        ensureCapacity(1);
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

    public void writePosition(Vector3i position) {
        writeLong(((long) (position.getX() & 0x3FFFFFF) << 38)
                | ((long) (position.getZ() & 0x3FFFFFF) << 12)
                | (position.getY() & 0xFFF));
    }

    public Vector3i readPosition() {
        long value = readLong();
        int x = (int) (value >> 38);
        int y = (int) (value << 52 >> 52);
        int z = (int) (value << 26 >> 38);
        return new Vector3i(x, y, z);
    }
}
