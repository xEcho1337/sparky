package net.echo.sparky.event.impl;

import net.echo.sparky.event.Listenable;

public class AsyncLoginStartEvent extends Listenable {

    private String name;

    public AsyncLoginStartEvent(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
