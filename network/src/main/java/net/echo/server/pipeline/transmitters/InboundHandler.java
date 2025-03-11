package net.echo.server.pipeline.transmitters;

public interface InboundHandler<C, P> extends Transmitter<C> {

    void read(C connection, P input);

    default void onChannelConnect(C connection) {
    }

    default void onChannelDisconnect(C connection) {
    }
}
