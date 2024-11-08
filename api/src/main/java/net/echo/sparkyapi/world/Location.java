package net.echo.sparkyapi.world;

import net.echo.sparkyapi.math.Vector;

public class Location extends Vector {

    private World world;
    private float yaw;
    private float pitch;

    public Location() {
    }

    public Location(World world, double x, double y, double z, float yaw, float pitch) {
        super(x, y, z);
        this.world = world;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public Location(World world, float yaw, float pitch) {
        this.world = world;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public float getYaw() {
        return yaw;
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }
}
