package net.echo.sparky.event.impl;

import net.echo.sparky.event.Listenable;

public class AsyncChatEvent extends Listenable {

    private String message;

    public AsyncChatEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
