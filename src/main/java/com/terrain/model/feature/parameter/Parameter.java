package com.terrain.model.feature.parameter;

// A parameter for a feature, which can be a number, a coordinate, or a boolean. This is an abstract class that is
// sealed to only allow the three subclasses. Because this is sealed, the compiler flags any switch that misses a type.
public sealed abstract class Parameter permits NumberParameter, CoordinateParameter, BooleanParameter {
    public final String label;

    protected Parameter(String label) {
        this.label = label;
    }
}
