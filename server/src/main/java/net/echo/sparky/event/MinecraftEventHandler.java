package net.echo.sparky.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@SuppressWarnings("all")
public class MinecraftEventHandler {

    private static class ListMap<E> extends HashMap<Class<? extends E>, List<Consumer<E>>> {
        // When you hate every 1.8 spigot fork so you lowkey rewrite it
    }

    private final ListMap<Event> handlers = new ListMap<>();

    public <T extends Event> void register(Class<T> clazz, Consumer<T> handler) {
        List<Consumer<Event>> callbacks = handlers.computeIfAbsent(clazz, k -> new ArrayList<>());

        callbacks.add((Consumer<Event>) handler);
    }

    public void call(Event event) {
        List<Consumer<Event>> callbacks = handlers.get(event.getClass());

        if (callbacks == null) return;

        for (Consumer<Event> callback : callbacks) {
            callback.accept(event);
        }
    }
}