package net.echo.sparkyapi.attribute;

public class Attribute<T> {

    private T value;

    public Attribute(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
