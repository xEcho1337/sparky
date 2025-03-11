package net.echo.server.attributes;

import org.checkerframework.checker.units.qual.A;

import java.util.function.Supplier;

public class Attribute<T> {

    private final String identifier;
    private T value;

    private Attribute(String identifier) {
        this.identifier = identifier;
    }

    public static <R> Attribute<R> of(String identifier) {
        return new Attribute<>(identifier);
    }

    public Attribute<T> copy() {
        return new Attribute<>(identifier);
    }

    public String getIdentifier() {
        return identifier;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
