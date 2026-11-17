package com.terrain.model.feature;

import com.terrain.model.TerrainMap;
import com.terrain.model.feature.parameter.CoordinateParameter;
import com.terrain.model.feature.parameter.Parameter;
import com.terrain.model.feature.parameter.ParameterSet;

import java.util.Random;

// A feature the user added (river, mountain, ...). Each instance owns its own parameters.
// To add a new feature: extend this, declare parameters with number()/coordinate()/bool(), implement apply(),
// then add it to FeatureType.
public abstract class Feature extends ParameterSet {
    public abstract String getName();

    // Must take all randomness from `random` (never Math.random()) so the same seed gives the same map.
    public abstract void apply(TerrainMap map, Random random);

    // Called when the world size changes or is set, so that coordinate parameters can clamp to the new size.
    public void setMapSize(int width, int height) {
        for (Parameter parameter : getParameters()) {
            if (parameter instanceof CoordinateParameter coordinate) {
                coordinate.setMapSize(width, height);
            }
        }
    }
}
