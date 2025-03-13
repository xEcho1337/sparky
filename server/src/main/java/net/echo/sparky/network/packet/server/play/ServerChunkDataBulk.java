package net.echo.sparky.network.packet.server.play;

import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparkyapi.world.chunk.Chunk;

import java.util.List;

public class ServerChunkDataBulk implements Packet.Server {

    private ServerChunkData.Extracted[] extracted;
    private int[] xPositions;
    private int[] zPositions;
    private boolean isOverworld;

    public ServerChunkDataBulk() {
    }

    public ServerChunkDataBulk(List<Chunk> chunks) {
        int amount = chunks.size();

        this.xPositions = new int[amount];
        this.zPositions = new int[amount];

        this.extracted = new ServerChunkData.Extracted[amount];
        this.isOverworld = true; // TODO: Support for multiple dimensions

        for (int j = 0; j < amount; ++j) {
            Chunk chunk = chunks.get(j);

            ServerChunkData.Extracted extractedData = ServerChunkData.extractData(chunk,
                    true, true, 65535);

            this.xPositions[j] = chunk.getX();
            this.zPositions[j] = chunk.getZ();

            this.extracted[j] = extractedData;
        }
    }

    @Override
    public void write(NetworkBuffer buffer) {

        buffer.writeBoolean(isOverworld);
        buffer.writeVarInt(extracted.length);

        for (int i = 0; i < xPositions.length; ++i) {
            buffer.writeInt(xPositions[i]);
            buffer.writeInt(zPositions[i]);
            buffer.writeShort(extracted[i].dataSize & 65535);
        }

        for (int j = 0; j < xPositions.length; ++j) {
            buffer.writeBytes(extracted[j].data);
        }
    }
}
