package net.echo.sparky.network.state;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.client.handshake.ClientHandshake;
import net.echo.sparky.network.packet.client.login.ClientLoginStart;
import net.echo.sparky.network.packet.client.handshake.ClientPing;
import net.echo.sparky.network.packet.client.handshake.ClientStatusRequest;
import net.echo.sparky.network.packet.client.play.*;
import net.echo.sparky.network.packet.server.handshake.ServerPong;
import net.echo.sparky.network.packet.server.login.ServerLoginDisconnect;
import net.echo.sparky.network.packet.server.login.ServerLoginSuccess;
import net.echo.sparky.network.packet.server.play.*;

import java.util.HashMap;
import java.util.Map;

public enum ConnectionState {

    HANDSHAKING {
        {
            this.registerPacket(0x00, PacketOwnership.CLIENT, ClientHandshake::new);
        }
    },
    STATUS {
        {
            this.registerPacket(0x00, PacketOwnership.CLIENT, ClientStatusRequest::new);
            this.registerPacket(0x01, PacketOwnership.CLIENT, ClientPing::new);
            this.registerPacket(0x00, PacketOwnership.SERVER, ServerStatusResponse::new);
            this.registerPacket(0x01, PacketOwnership.SERVER, ServerPong::new);
        }
    },
    LOGIN {
        {
            this.registerPacket(0x00, PacketOwnership.CLIENT, ClientLoginStart::new);
            this.registerPacket(0x00, PacketOwnership.SERVER, ServerLoginDisconnect::new);
            this.registerPacket(0x02, PacketOwnership.SERVER, ServerLoginSuccess::new);
        }
    },
    PLAY {
        {
            this.registerPacket(0x00, PacketOwnership.CLIENT, ClientKeepAlive::new);
            this.registerPacket(0x01, PacketOwnership.CLIENT, ClientChatMessage::new);
            this.registerPacket(0x02, PacketOwnership.CLIENT, ClientUseEntity::new);
            this.registerPacket(0x03, PacketOwnership.CLIENT, ClientPositionIdle::new);
            this.registerPacket(0x04, PacketOwnership.CLIENT, ClientPosition::new);
            this.registerPacket(0x05, PacketOwnership.CLIENT, ClientLook::new);
            this.registerPacket(0x06, PacketOwnership.CLIENT, ClientPositionLook::new);
            this.registerPacket(0x07, PacketOwnership.CLIENT, ClientPlayerDigging::new);
            this.registerPacket(0x09, PacketOwnership.CLIENT, ClientHeldItemChange::new);
            this.registerPacket(0x0A, PacketOwnership.CLIENT, ClientArmSwing::new);
            this.registerPacket(0x15, PacketOwnership.CLIENT, ClientSettings::new);
            this.registerPacket(0x17, PacketOwnership.CLIENT, ClientPluginMessage::new);
            // this.registerPacket(0x08, PacketOwnership.CLIENT, ClientBlockPlacement::new);

            this.registerPacket(0x00, PacketOwnership.SERVER, ServerKeepAlive::new);
            this.registerPacket(0x01, PacketOwnership.SERVER, ServerJoinGame::new);
            this.registerPacket(0x02, PacketOwnership.SERVER, ServerChatMessage::new);
            this.registerPacket(0x03, PacketOwnership.SERVER, ServerTimeUpdate::new);
            this.registerPacket(0x05, PacketOwnership.SERVER, ServerSpawnPosition::new);
            this.registerPacket(0x06, PacketOwnership.SERVER, ServerUpdateHealth::new);
            this.registerPacket(0x07, PacketOwnership.SERVER, ServerRespawn::new);
            this.registerPacket(0x08, PacketOwnership.SERVER, ServerPositionAndLook::new);
            this.registerPacket(0x21, PacketOwnership.SERVER, ServerChunkData::new);
        }
    };

    private final Map<PacketOwnership, BiMap<Integer, PacketFactory<? extends Packet>>> directionMap = new HashMap<>();
    private final Map<PacketOwnership, BiMap<Integer, Class<? extends Packet>>> classMap = new HashMap<>();

    public <T extends Packet> void registerPacket(int id, PacketOwnership direction, PacketFactory<T> packetFactory) {
        var map = directionMap.computeIfAbsent(direction, k -> HashBiMap.create());
        var classBiMap = classMap.computeIfAbsent(direction, k -> HashBiMap.create());

        map.put(id, packetFactory);
        classBiMap.put(id, packetFactory.create().getClass());
    }

    public Packet getPacketFromId(PacketOwnership direction, int id) {
        BiMap<Integer, PacketFactory<? extends Packet>> map = directionMap.get(direction);

        if (map == null) return null;

        PacketFactory<? extends Packet> packetFactory = map.get(id);

        return packetFactory == null ? null : packetFactory.create();
    }

    public int getIdFromPacket(PacketOwnership direction, Packet packet) {
        return classMap.get(direction).inverse().get(packet.getClass());
    }
}

