package net.echo.sparky.network.player;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {

    private final Map<Channel, PlayerConnection> connectionsMap = new ConcurrentHashMap<>();

    public PlayerConnection getConnection(Channel channel) {
        return connectionsMap.get(channel);
    }

    public void addConnection(PlayerConnection connection) {
        connectionsMap.put(connection.getChannel(), connection);
    }

    public void removeConnection(PlayerConnection connection) {
        connectionsMap.remove(connection.getChannel());
    }

    public Collection<PlayerConnection> getAll() {
        return connectionsMap.values();
    }
}
