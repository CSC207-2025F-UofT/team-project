package com.terrain.model.feature.parameter;

public final class BooleanParameter extends Parameter {
    private boolean value;

    public BooleanParameter(String label, boolean value) {
        super(label);
        this.value = value;
    }

    public boolean getValue() {
        return value;
    }

    public void setValue(boolean value) {
        this.value = value;
    }
}
