package net.echo.server.handlers;

import org.apache.logging.log4j.Logger;

import java.nio.channels.CompletionHandler;

public record FlushHandler(Logger logger) implements CompletionHandler<Integer, Object> {

    @Override
    public void completed(Integer result, Object attachment) {
        if (result == -1) {
            logger.error("Failed to write!");
        }
    }

    @Override
    public void failed(Throwable exc, Object attachment) {
        logger.error("Exception caught while flushing!", exc);
    }
}
