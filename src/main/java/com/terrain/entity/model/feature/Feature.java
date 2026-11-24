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

    // a feature that implements BlobShaped will use these parameters as well
    protected final BlobParameters createBlobParameters() {
        return new BlobParameters(
                number("Radius", 50, 1, 500),
                number("Noise Amplitude", 50.0, 0.0, 100.0),
                number("Noise Scale", 30.0, 0.01, 100.0));
    }

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
