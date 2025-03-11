package net.echo.server.attributes;

public class Attribute<T> {

    private final String identifier;
    private T value;

    public Attribute(String identifier) {
        this.identifier = identifier;
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
