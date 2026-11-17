package com.terrain.entity.model.feature.parameter;

import java.util.ArrayList;
import java.util.List;

// base class for anything that owns an ordered list of parameters (features, world settings). Declaring a parameter
// with number()/coordinate()/bool() registers it automatically, in declaration order, so none can be forgotten.
public abstract class ParameterSet {
    private final List<Parameter> parameters = new ArrayList<>();

    public List<Parameter> getParameters() {
        return parameters;
    }

    protected NumberParameter number(String label, double value, double min, double max) {
        return add(new NumberParameter(label, value, min, max));
    }

    protected CoordinateParameter coordinate(String label, double x, double y) {
        return add(new CoordinateParameter(label, x, y));
    }

    protected BooleanParameter bool(String label, boolean value) {
        return add(new BooleanParameter(label, value));
    }

    // adds the parameter to the list and returns it, so the caller can keep a reference to it.
    private <P extends Parameter> P add(P parameter) {
        parameters.add(parameter);
        return parameter;
    }
}
