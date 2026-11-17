package com.terrain.model.feature.parameter;

public final class NumberParameter extends Parameter {
    public final double min;
    public final double max;
    private double value;

    public NumberParameter(String label, double value, double min, double max) {
        super(label);
        this.min = min;
        this.max = max;
        setValue(value);
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = Math.clamp(value, min, max);
    }
}
