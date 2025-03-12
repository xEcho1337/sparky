package net.echo.sparky.network.packet.server.play;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import net.echo.server.buffer.NetworkBuffer;
import net.echo.sparky.network.packet.Packet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;

public class ServerStatusResponse implements Packet.Server {

    private Players players;
    private Version version;
    private Description description;

    public ServerStatusResponse() {
    }

    public ServerStatusResponse(Players players, Version version, Description description) {
        this.players = players;
        this.version = version;
        this.description = description;
    }

    @Override
    public void write(NetworkBuffer buffer) {
        buffer.writeString(toJson());
    }

    public record Players(int max, int online) {
    }

    public record Version(String name, int protocol) {
    }

    public record Description(Component text) {
    }

    private String toJson() {
        Gson gson = new Gson();
        JsonObject json = new JsonObject();

        JsonObject versionJson = new JsonObject();
        versionJson.addProperty("name", version.name());
        versionJson.addProperty("protocol", version.protocol());

        JsonObject playersJson = new JsonObject();
        playersJson.addProperty("max", players.max());
        playersJson.addProperty("online", players.online());

        String serialized = GsonComponentSerializer.gson().serialize(description.text());
        JsonObject descriptionJson = gson.fromJson(serialized, JsonObject.class);

        json.add("version", versionJson);
        json.add("players", playersJson);
        json.add("description", descriptionJson);

        return gson.toJson(json);
    }
}
