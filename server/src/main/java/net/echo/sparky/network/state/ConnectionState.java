package net.echo.sparky.network.state;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import it.unimi.dsi.fastutil.Hash;
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

public enum ConnectionState {

    HANDSHAKING {
        {
            PacketOwnership.CLIENT.register(0x00, ClientHandshake::new);
        }
    },
    STATUS {
        {
            PacketOwnership.CLIENT.register(0x00, ClientStatusRequest::new);
            PacketOwnership.CLIENT.register(0x01, ClientPing::new);
            PacketOwnership.SERVER.register(0x00, ServerStatusResponse::new);
            PacketOwnership.SERVER.register(0x01, ServerPong::new);
        }
    },
    LOGIN {
        {
            PacketOwnership.CLIENT.register(0x00, ClientLoginStart::new);
            PacketOwnership.SERVER.register(0x00, ServerLoginDisconnect::new);
            PacketOwnership.SERVER.register(0x02, ServerLoginSuccess::new);
        }
    },
    PLAY {
        {
            PacketOwnership.CLIENT.register(0x00, ClientKeepAlive::new);
            PacketOwnership.CLIENT.register(0x01, ClientChatMessage::new);
            PacketOwnership.CLIENT.register(0x02, ClientUseEntity::new);
            PacketOwnership.CLIENT.register(0x03, ClientPlayerIdle::new);
            PacketOwnership.CLIENT.register(0x04, ClientPlayerPosition::new);
            PacketOwnership.CLIENT.register(0x05, ClientPlayerLook::new);
            PacketOwnership.CLIENT.register(0x06, ClientPlayerPositionAndLook::new);
            PacketOwnership.CLIENT.register(0x07, ClientPlayerDigging::new);
            // PacketOwnership.CLIENT.register(0x08, ClientBlockPlacement::new);
            PacketOwnership.CLIENT.register(0x09, ClientHeldItemChange::new);
            PacketOwnership.CLIENT.register(0x0A, ClientArmSwing::new);
            PacketOwnership.CLIENT.register(0x0B, ClientEntityAction::new);
            PacketOwnership.CLIENT.register(0x13, ClientPlayerAbilities::new);
            PacketOwnership.CLIENT.register(0x15, ClientSettings::new);
            PacketOwnership.CLIENT.register(0x16, ClientStatus::new);
            PacketOwnership.CLIENT.register(0x17, ClientPluginMessage::new);

            PacketOwnership.SERVER.register(0x00, ServerKeepAlive::new);
            PacketOwnership.SERVER.register(0x01, ServerJoinGame::new);
            
            PacketOwnership.SERVER.register(0x01, ServerJoinGame::new);
            PacketOwnership.SERVER.register(0x02, ServerChatMessage::new);
            PacketOwnership.SERVER.register(0x03, ServerTimeUpdate::new);
            PacketOwnership.SERVER.register(0x05, ServerSpawnPosition::new);
            PacketOwnership.SERVER.register(0x06, ServerUpdateHealth::new);
            PacketOwnership.SERVER.register(0x07, ServerRespawn::new);
            PacketOwnership.SERVER.register(0x08, ServerPositionAndLook::new);
            PacketOwnership.SERVER.register(0x21, ServerChunkData::new);
            PacketOwnership.SERVER.register(0x40, ServerDisconnect::new);
        }
    };

    public Optional<Packet> getPacketFromId(PacketOwnership direction, int id) {
        PacketFactory<? extends Packet> factory = direction.getFactoryMap().get(id);

        if (factory == null) return Optional.empty();

        return Optional.ofNullable(factory.create());
    }

    public int getIdFromPacket(PacketOwnership direction, Packet packet) {
        return direction.getPacketIdMap().get(packet.getClass());
    }
}

