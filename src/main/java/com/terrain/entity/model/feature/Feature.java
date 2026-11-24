package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.Parameter;
import com.terrain.entity.model.feature.parameter.ParameterSet;

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

    public static double getDistance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
