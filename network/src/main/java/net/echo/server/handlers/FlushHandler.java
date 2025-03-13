package net.echo.server.handlers;

import net.echo.server.channel.Channel;
import org.apache.logging.log4j.Logger;

import java.nio.channels.CompletionHandler;

public record FlushHandler(Logger logger) implements CompletionHandler<Integer, Channel> {

    @Override
    public void completed(Integer result, Channel attachment) {
        if (result == -1) {
            logger.error("Failed to write!");
        }
    }

    @Override
    public void failed(Throwable exc, Channel attachment) {
        logger.error("Exception caught while flushing!", exc);
        attachment.close();
    }
}
