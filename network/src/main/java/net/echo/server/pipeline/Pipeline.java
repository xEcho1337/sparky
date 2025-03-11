package net.echo.server.pipeline;

import net.echo.server.pipeline.transmitters.Transmitter;

import java.util.ArrayList;
import java.util.List;

public class Pipeline<C> {

    private final List<Transmitter<C>> transmitters;

    public Pipeline() {
        this.transmitters = new ArrayList<>();
    }

    public <T extends Transmitter<C>> Pipeline<C> append(T transmitter) {
        this.transmitters.add(transmitter);
        return this;
    }

    public List<Transmitter<C>> getTransmitters() {
        return transmitters;
    }
}
