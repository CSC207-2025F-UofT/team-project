package com.terrain.model.feature.parameter;

// An (x, y) point, in map tiles.
public final class CoordinateParameter extends Parameter {
    private double x;
    private double y;
    private double maxX = Double.POSITIVE_INFINITY;
    private double maxY = Double.POSITIVE_INFINITY;

    public CoordinateParameter(String label, double x, double y) {
        super(label);
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void set(double x, double y) {
        this.x = Math.clamp(x, 0, maxX);
        this.y = Math.clamp(y, 0, maxY);
    }

    // Coordinates are indices of a tile, so the largest valid coordinate is dimension - 1.
    public void setMapSize(int width, int height) {
        maxX = Math.max(0, width - 1);
        maxY = Math.max(0, height - 1);
        set(x, y);
    }

}
