package net.echo.sparky.network.state;

import net.echo.sparky.network.packet.Packet;

import java.util.HashMap;
import java.util.Map;

public enum PacketOwnership {

    CLIENT, SERVER;

    private final Map<Integer, PacketFactory<? extends Packet>> factoryMap;
    private final Map<Integer, Class<? extends Packet>> packetClassMap;
    private final Map<Class<? extends Packet>, Integer> packetIdMap;

    PacketOwnership() {
        this.factoryMap = new HashMap<>();
        this.packetClassMap = new HashMap<>();
        this.packetIdMap = new HashMap<>();
    }

    public Map<Integer, PacketFactory<? extends Packet>> getFactoryMap() {
        return factoryMap;
    }

    public Map<Integer, Class<? extends Packet>> getPacketClassMap() {
        return packetClassMap;
    }

    public Map<Class<? extends Packet>, Integer> getPacketIdMap() {
        return packetIdMap;
    }

    public <T extends Packet> void register(int id, PacketFactory<T> packetFactory) {
        factoryMap.put(id, packetFactory);
        packetClassMap.put(id, packetFactory.create().getClass());
        packetIdMap.put(packetFactory.create().getClass(), id);
    }
}
