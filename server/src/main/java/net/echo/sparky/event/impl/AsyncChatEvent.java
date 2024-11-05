package net.echo.sparky.event.impl;

import net.echo.sparky.event.Cancellable;

public class AsyncChatEvent extends Cancellable {

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
