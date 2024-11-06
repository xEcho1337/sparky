package net.echo.sparky.event.impl;

import net.echo.sparky.event.Cancellable;

import java.util.UUID;

public class AsyncPreLoginEvent extends Cancellable {

    private String name;
    private UUID uuid;

    public AsyncPreLoginEvent(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }
}
