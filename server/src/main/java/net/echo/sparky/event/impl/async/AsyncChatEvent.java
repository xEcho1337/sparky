package net.echo.sparky.event.impl.async;

import net.echo.sparky.event.Cancellable;

public class AsyncChatEvent extends Cancellable {

    private String message;
    private String format;

    public AsyncChatEvent(String message, String format) {
        this.message = message;
        this.format = format;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }
}
