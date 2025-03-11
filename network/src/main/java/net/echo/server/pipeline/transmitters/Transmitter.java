package net.echo.server.pipeline.transmitters;

import java.io.IOException;

@SuppressWarnings("all")
public interface Transmitter<C> {

    void handleException(C connection, Exception exception);

    interface In<C, I, O> extends Transmitter<C> {
        O read(C connection, I buffer) throws IOException;

        default Object readObject(C connection, Object input) {
            try {
                return read(connection, (I) input);
            } catch (Exception e) {
                handleException(connection, e);
                return null;
            }
        }
    }

    interface Out<C, I, O> extends Transmitter<C> {
        O write(C connection, I buffer) throws IOException;

        default Object writeObject(C connection, Object input) {
            try {
                return write(connection, (I) input);
            } catch (Exception e) {
                handleException(connection, e);
                return null;
            }
        }
    }
}
