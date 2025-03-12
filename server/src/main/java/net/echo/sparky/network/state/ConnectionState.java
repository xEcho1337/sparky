package net.echo.sparky.network.state;

import net.echo.sparky.network.packet.Packet;
import net.echo.sparky.network.packet.client.handshake.ClientHandshake;
import net.echo.sparky.network.packet.client.handshake.ClientPing;
import net.echo.sparky.network.packet.client.handshake.ClientStatusRequest;
import net.echo.sparky.network.packet.client.login.ClientLoginStart;
import net.echo.sparky.network.packet.client.play.*;
import net.echo.sparky.network.packet.server.handshake.ServerPong;
import net.echo.sparky.network.packet.server.login.ServerLoginDisconnect;
import net.echo.sparky.network.packet.server.login.ServerLoginSuccess;
import net.echo.sparky.network.packet.server.play.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static net.echo.sparky.network.state.PacketOwnership.*;

public enum ConnectionState {

    HANDSHAKING {
        {
            register(CLIENT, 0x00, ClientHandshake::new);
        }
    },
    STATUS {
        {
            register(CLIENT, 0x00, ClientStatusRequest::new);
            register(CLIENT, 0x01, ClientPing::new);
            register(SERVER, 0x00, ServerStatusResponse::new);
            register(SERVER, 0x01, ServerPong::new);
        }
    },
    LOGIN {
        {
            register(CLIENT, 0x00, ClientLoginStart::new);
            register(SERVER, 0x00, ServerLoginDisconnect::new);
            register(SERVER, 0x02, ServerLoginSuccess::new);
        }
    },
    PLAY {
        {
            register(CLIENT, 0x00, ClientKeepAlive::new);
            register(CLIENT, 0x01, ClientChatMessage::new);
            register(CLIENT, 0x02, ClientUseEntity::new);
            register(CLIENT, 0x03, ClientPlayerIdle::new);
            register(CLIENT, 0x04, ClientPlayerPosition::new);
            register(CLIENT, 0x05, ClientPlayerLook::new);
            register(CLIENT, 0x06, ClientPlayerPositionAndLook::new);
            register(CLIENT, 0x07, ClientPlayerDigging::new);
            // register(CLIENT, 0x08, ClientBlockPlacement::new);
            register(CLIENT, 0x09, ClientHeldItemChange::new);
            register(CLIENT, 0x0A, ClientArmSwing::new);
            register(CLIENT, 0x0B, ClientEntityAction::new);
            register(CLIENT, 0x13, ClientPlayerAbilities::new);
            register(CLIENT, 0x15, ClientSettings::new);
            register(CLIENT, 0x16, ClientStatus::new);
            register(CLIENT, 0x17, ClientPluginMessage::new);

            register(SERVER, 0x00, ServerKeepAlive::new);
            register(SERVER, 0x01, ServerJoinGame::new);
            
            register(SERVER, 0x01, ServerJoinGame::new);
            register(SERVER, 0x02, ServerChatMessage::new);
            register(SERVER, 0x03, ServerTimeUpdate::new);
            register(SERVER, 0x05, ServerSpawnPosition::new);
            register(SERVER, 0x06, ServerUpdateHealth::new);
            register(SERVER, 0x07, ServerRespawn::new);
            register(SERVER, 0x08, ServerPositionAndLook::new);
            register(SERVER, 0x21, ServerChunkData::new);
            register(SERVER, 0x40, ServerDisconnect::new);
        }
    };

    private final Map<PacketOwnership, PacketRegistry> registryMap = new HashMap<>();

    public <T extends Packet> void register(PacketOwnership ownership, int id, PacketFactory<T> packetFactory) {
        PacketRegistry registry = registryMap.containsKey(ownership) ? registryMap.get(ownership) : new PacketRegistry();

        registry.register(id, packetFactory);
        registryMap.put(ownership, registry);
    }
    
    public Optional<Packet> getPacketFromId(PacketOwnership direction, int id) {
        PacketRegistry registry = registryMap.get(direction);
        PacketFactory<? extends Packet> factory = registry.getFactoryMap().get(id);

        if (factory == null) return Optional.empty();

        return Optional.ofNullable(factory.create());
    }

    public int getIdFromPacket(PacketOwnership direction, Packet packet) {
        var map = registryMap.get(direction).getPacketIdMap();
        return map.get(packet.getClass());
    }
}

