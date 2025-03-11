package net.echo.server.pipeline.transmitters;

import java.io.IOException;

public interface Transmitter<C> {

    void handleException(C connection, Exception exception);

    interface In<C, I, O> extends Transmitter<C> {
        default Object readBasic(C connection, Object input) throws IOException {
            return read(connection, (I) input);
        }

        O read(C connection, I buffer) throws IOException;
    }

    interface Out<C, I, O> extends Transmitter<C> {
        default Object writeBasic(C connection, Object input) throws IOException {
            return write(connection, (I) input);
        }

        O write(C connection, I buffer) throws IOException;
    }
}
