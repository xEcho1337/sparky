package net.echo.server.pipeline;

import net.echo.server.pipeline.handler.InboundHandler;
import net.echo.server.pipeline.transmitters.Transmitter;

import java.util.ArrayList;
import java.util.List;

public class Pipeline<C> {

    private final List<Transmitter<C>> transmitters;
    private final List<InboundHandler<C, ?>> handlers;

    public Pipeline() {
        this.transmitters = new ArrayList<>();
        this.handlers = new ArrayList<>();
    }

    public <T extends Transmitter<C>> Pipeline<C> addTransmitter(T transmitter) {
        this.transmitters.add(transmitter);
        return this;
    }

    public <T extends InboundHandler<C, ?>> Pipeline<C> addHandler(T handler) {
        this.handlers.add(handler);
        return this;
    }

    public List<Transmitter<C>> getTransmitters() {
        return transmitters;
    }

    public List<InboundHandler<C, ?>> getHandlers() {
        return handlers;
    }
}
