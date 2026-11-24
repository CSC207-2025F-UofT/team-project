package com.terrain.entity.model;

public enum TileType {
    GRASS {
        @Override
        Rgb colorAtPercent(double t) {
            return new Rgb(0.1, 0.3 + 0.5 * t, 0.1);
        }
    },
    WATER {
        @Override
        Rgb colorAtPercent(double t) {
            return new Rgb(0.0, 0.2 + 0.3 * t, 0.4 + 0.4 * t);
        }
    },
    STONE {
        @Override
        Rgb colorAtPercent(double t) {
            return new Rgb(0.3 + 0.4 * t, 0.3 + 0.4 * t, 0.3 + 0.4 * t);
        }
    },
    SAND {
        @Override
        Rgb colorAtPercent(double t) {
            return new Rgb(0.8 + 0.2 * t, 0.7 + 0.2 * t, 0.5 + 0.2 * t);
        }
    },
    SNOW {
        @Override
        Rgb colorAtPercent(double t) {
            return new Rgb(1.0, 1.0, 1.0);
        }
    },
    FOREST {
        @Override
        Rgb colorAtPercent(double t) {
            return new Rgb(0.0, 0.5 + 0.5 * t, 0.0);
        }
    },
    SWAMP {
        @Override
        Rgb colorAtPercent(double t) {
            return new Rgb(0.0, 0.3 + 0.4 * t, 0.0);
        }
    };

    // returns the color of this tile type at a given elevation, normalized for elevation range
    public Rgb colorAt(double elevation, double minElevation, double maxElevation) {
        double t = maxElevation == minElevation ? 0.0 : (elevation - minElevation) / (maxElevation - minElevation);
        return colorAtPercent(Math.clamp(t, 0.0, 1.0));
    }

    // every TileType must implement this method
    abstract Rgb colorAtPercent(double t);

    // simple data class to hold RGB values
    public record Rgb(double red, double green, double blue) {
        public Rgb {
            if (red < 0.0 || red > 1.0 || green < 0.0 || green > 1.0 || blue < 0.0 || blue > 1.0) {
                throw new IllegalArgumentException("RGB values must be in the range [0.0, 1.0]");
            }
        }

        // converts the RGB values to an array for use with JavaFX Color
        public double[] toArray() {
            return new double[]{red, green, blue};
        }
    }
}