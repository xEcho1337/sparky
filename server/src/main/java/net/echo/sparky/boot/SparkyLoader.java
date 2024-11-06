package net.echo.sparky.boot;

import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.MinecraftEventHandler;
import net.echo.sparky.event.impl.LoginEvent;
import net.echo.sparky.world.generator.ChunkProvider;
import net.echo.sparky.world.generator.unit.impl.FlatWorldGenerator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SparkyLoader {

    public static void main(String[] args) {
        MinecraftServer server = new MinecraftServer();

        ChunkProvider provider = server.getChunkProvider();
        provider.setGenerator(new FlatWorldGenerator(4));

        MinecraftEventHandler eventHandler = server.getEventHandler();

        eventHandler.register(LoginEvent.class, event -> {
            server.broadcast(Component.text("Hello, " + event.getPlayer().getName() + "!").color(NamedTextColor.YELLOW));
        });

        server.start();
    }
}
