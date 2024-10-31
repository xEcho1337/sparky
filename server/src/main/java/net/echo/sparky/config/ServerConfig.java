package net.echo.sparky.config;

import com.moandjiezana.toml.Toml;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.io.*;

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

    public int getPort() {
        return Math.toIntExact(toml.getLong("port"));
    }

    public Component getMotd() {
        return deserializer.deserialize(toml.getString("motd"));
    }
}
