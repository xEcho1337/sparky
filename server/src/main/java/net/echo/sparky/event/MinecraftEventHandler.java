package net.echo.sparky.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class MinecraftEventHandler {

    private static class ListMap<E> extends HashMap<Class<? extends E>, List<Consumer<? extends E>>> {
        // When you hate every 1.8 spigot fork so you lowkey rewrite it
    }

    private final ListMap<Listenable> handlers = new ListMap<>();

    public <T extends Listenable> void register(Class<T> clazz, Consumer<T> handler) {
        List<Consumer<? extends Listenable>> callbacks =
                handlers.computeIfAbsent(clazz, k -> new ArrayList<>());

        callbacks.add(handler);
    }
}