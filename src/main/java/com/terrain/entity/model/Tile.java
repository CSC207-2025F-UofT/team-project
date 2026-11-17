package com.terrain.entity.model;

public class Tile {
    private double elevation;
    private TileType type;
    private TileType finalType; // Type after elevation has been considered.

    public Tile() {
        elevation = 0;
    }

    public double getElevation() {
        return elevation;
    }

    public void setElevation(double elevation) {
        this.elevation = elevation;
    }

    // TODO: MAYBE: Add other properties like moisture, temperature, etc. depending on the needs of the terrain
    //  generation. or, Tile.type could use an enum that includes different types of terrain (e.g., water, grass,
    //  stone, etc.)
}
