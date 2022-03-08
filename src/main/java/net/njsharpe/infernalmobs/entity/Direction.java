package net.njsharpe.infernalmobs.entity;

public class Direction {

    private final float dX;
    private final float dY;
    private final float dZ;
    private final float distance;

    public Direction(float dX, float dY, float dZ, float distance) {
        this.dX = dX;
        this.dY = dY;
        this.dZ = dZ;
        this.distance = distance;
    }

    public float getDeltaX() {
        return this.dX;
    }

    public float getDeltaY() {
        return this.dY;
    }

    public float getDeltaZ() {
        return this.dZ;
    }

    public float getDistance() {
        return this.distance;
    }
}
