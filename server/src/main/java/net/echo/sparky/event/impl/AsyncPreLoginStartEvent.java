package net.echo.sparky.event.impl;

import net.echo.sparky.event.Cancellable;

public class AsyncPreLoginStartEvent extends Cancellable {

    private String name;

    public AsyncPreLoginStartEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
