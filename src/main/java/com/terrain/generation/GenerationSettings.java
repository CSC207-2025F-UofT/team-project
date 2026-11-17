package com.terrain.generation;

import com.terrain.model.feature.Feature;
import com.terrain.model.feature.FeatureType;

import java.util.ArrayList;
import java.util.List;
import java.util.random.RandomGenerator;

// Everything the user controls: seed, world settings, and the ordered list of features.
public class GenerationSettings {
    public long seed = RandomGenerator.getDefault().nextLong(); // random unless the user sets one
    public final WorldSetting world = new WorldSetting();
    public final List<Feature> features = new ArrayList<>();

    // Backs the "new feature" button.
    public Feature addFeature(FeatureType type) {
        Feature feature = type.create();
        features.add(feature);
        return feature;
    }

    public void removeFeature(Feature feature) {
        features.remove(feature);
    }

    // TODO: reorder features, etc. (for the UI)
}
