package net.echo.server.pipeline.handler;

/**
 * Interface for handling inbound messages parsed by transmitters.
 *
 * @param <C> The remote connection type
 * @param <P> The input type
 */
@SuppressWarnings("all")
public interface InboundHandler<C, P> {

    /**
     * Handles a message from the connection.
     *
     * @param connection The remote connection
     * @param input The message
     */
    void handle(C connection, P input);

    /**
     * Used to convert {@link Object}s to the input type.
     *
     * @param connection The remote connection
     * @param input The message
     */
    default void handleObject(C connection, Object input) {
        handle(connection, (P) input);
    }

    /**
     * Method called when an exception occurs.
     *
     * @param connection The remote connection
     * @param exception The exception
     */
    default void handleException(C connection, Exception exception) {
    }

    /**
     * Method called when a connection is established.
     * @param connection The remote connection
     */
    default void onChannelConnect(C connection) {
    }

    /**
     * Method called when a connection is closed.
     * @param connection The remote connection
     */
    default void onChannelDisconnect(C connection) {
    }
}
