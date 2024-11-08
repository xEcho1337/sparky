package net.echo.sparky.network.packet.server.play;

import net.echo.sparky.network.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.world.chunk.ChunkColumn;
import net.echo.sparky.world.chunk.ChunkSection;
import net.echo.sparkyapi.world.chunk.Section;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerChunkData implements Packet.Server {

    private ChunkColumn chunk;
    private Extracted extractedData;
    private boolean groundUpContinuous;

    public ServerChunkData() {
    }

    public ServerChunkData(ChunkColumn chunk, boolean groundUpContinuous) {
        this.chunk = chunk;
        this.groundUpContinuous = groundUpContinuous;
        this.extractedData = extractData(chunk, groundUpContinuous, 65535);
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeInt(chunk.getX());
        buffer.writeInt(chunk.getZ());
        buffer.writeBoolean(groundUpContinuous);
        buffer.writeShort(extractedData.dataSize);
        buffer.writeVarInt(extractedData.data.length);
        buffer.writeBytes(extractedData.data);
    }

    public static Extracted extractData(ChunkColumn chunkColumn, boolean groundUpContinuous, int blockMask) {
        Extracted extracted = new Extracted();
        List<Section> nonEmptySections = new ArrayList<>();

        for (int y = 0; y < 16; y++) {
            Section section = chunkColumn.getSection(y);

            if (section != null && (!groundUpContinuous || section.getNonAirBlocks() > 0) && (blockMask & 1 << y) != 0) {
                extracted.dataSize |= 1 << y;
                nonEmptySections.add(section);
            }
        }

        extracted.data = new byte[calculateDataSize(nonEmptySections.size(), false, groundUpContinuous)];
        int offset = 0;

        for (Section section : nonEmptySections) {
            char[] data = section.getBlocks();

            for (char block : data) {
                extracted.data[offset++] = (byte) (block & 255);
                extracted.data[offset++] = (byte) (block >> 8 & 255);
            }
        }

        for (Section section : nonEmptySections) {
            byte[] blockLightData = new byte[2048];

            Arrays.fill(blockLightData, (byte) 1);

            offset = copyArray(blockLightData, extracted.data, offset);
        }

        if (groundUpContinuous) {
            copyArray(new byte[256], extracted.data, offset);
        }

        return extracted;
    }

    private static int copyArray(final byte[] source, final byte[] destination, final int offset) {
        System.arraycopy(source, 0, destination, offset, source.length);
        return offset + source.length;
    }

    protected static int calculateDataSize(int sectionsCount, boolean hasSky, boolean groundUpContinuous) {
        final int blockDataSize = sectionsCount * 8192;
        final int blockDataSizeHalf = sectionsCount * 2048;
        final int blocklightSize = hasSky ? sectionsCount * 2048 : 0;
        final int skyLightSize = groundUpContinuous ? 256 : 0;
        return blockDataSize + blockDataSizeHalf + blocklightSize + skyLightSize;
    }

    public static class Extracted {
        public byte[] data;
        public int dataSize;
    }
}
