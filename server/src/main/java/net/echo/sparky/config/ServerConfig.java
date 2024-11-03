package net.echo.sparky.config;

import com.moandjiezana.toml.Toml;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.*;
import java.util.Map;

public class ServerConfig {

    private final MiniMessage deserializer = MiniMessage.miniMessage();
    private Toml toml = new Toml();

    public void load(File file) {
        if (!file.exists()) {
            createFromResources(file);
        }

        toml = toml.read(file);
    }

    private void createFromResources(File file) {
        try (InputStream resource = ServerConfig.class.getResourceAsStream("/server.toml");
             FileWriter fileWriter = new FileWriter(file);
             BufferedWriter writer = new BufferedWriter(fileWriter)) {

            if (resource == null) {
                throw new FileNotFoundException("server.toml was not found.");
            }

            writer.write(new String(resource.readAllBytes()));
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public String getBrand() {
        return toml.getString("brand");
    }

    public int getPort() {
        return Math.toIntExact(toml.getLong("port"));
    }

    public Component getMotd() {
        return deserializer.deserialize(toml.getString("motd"));
    }

    public int getNettyThreads() {
        return Math.toIntExact(toml.getLong("netty_threads"));
    }

    public int getTickRate() {
        return Math.toIntExact(toml.getLong("ticks_per_second"));
    }

    public int getDifficulty() {
        return Math.toIntExact(toml.getLong("difficulty"));
    }

    public int getMaxPlayers() {
        return Math.toIntExact(toml.getLong("max_players"));
    }

    public String getPingVersionHover() {
        return toml.getString("ping_version_hover");
    }

    public int getRenderDistance() {
        return Math.toIntExact(toml.getLong("render_distance"));
    }
}
