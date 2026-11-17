package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.feature.parameter.BooleanParameter;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;

public class MountainFeature extends Feature {
    private final NumberParameter height = number("Height", 100, 10, 1000);
    private final NumberParameter radius = number("Radius", 50, 1, 500);
    private final CoordinateParameter peakLocation = coordinate("Peak location", 0, 0);
    private final BooleanParameter snowCap = bool("Snow cap", true);
    // I actually don't like the idea of snow caps being a boolean, but it's a simple example for now.

    @Override
    public String getName() {
        return "Mountain";
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        // this is how the parameters are accessed
        double h = height.getValue();          // NumberParameter -> double
        double r = radius.getValue();
        double px = peakLocation.getX();       // CoordinateParameter -> getX()/getY()
        double py = peakLocation.getY();       // already clamped to the map size
        boolean cap = snowCap.getValue();      // BooleanParameter -> boolean

        // TODO: raise elevation around the peak

    }
}
