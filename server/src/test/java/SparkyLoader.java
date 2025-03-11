import net.echo.sparky.MinecraftServer;
import net.echo.sparky.event.MinecraftEventHandler;
import net.echo.sparky.event.impl.login.LoginEvent;
import net.echo.sparky.world.generator.ChunkProvider;
import net.echo.sparky.world.generator.unit.impl.FlatWorldGenerator;
import net.echo.sparkyapi.player.GameProfile;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

public class SparkyLoader {

    public static void main(String[] args) {
        MinecraftServer server = MinecraftServer.getInstance();

        ChunkProvider provider = server.getChunkProvider();
        provider.setGenerator(new FlatWorldGenerator(4));

        MinecraftEventHandler eventHandler = server.getEventHandler();

        eventHandler.register(LoginEvent.class, event -> {
            GameProfile profile = event.getPlayer().getGameProfile();
            server.broadcast(Component.text("Hello, " + profile.getName() + "!").color(NamedTextColor.YELLOW));
        });

        server.start();
    }
}
