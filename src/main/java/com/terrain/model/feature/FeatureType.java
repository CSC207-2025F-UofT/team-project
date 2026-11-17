package com.terrain.model.feature;

import java.util.function.Supplier;

// The list of available features, used to fill the dropdown and to create new instances.
public enum FeatureType {
    MOUNTAIN(MountainFeature::new),
    RIVER(RiverFeature::new);
    // `MountainFeature::new` is a shortcut for `() -> new MountainFeature()`, which just makes a new instance of the
    // feature. This is a "factory" that we can call to create new instances of the feature. We do this so that we can
    // have a kind of dropdown menu of features, and we can add new features without having to change the code that
    // creates them.

    // We would use it like: `Feature mountain = FeatureType.MOUNTAIN.create()` to create a new instance of the
    // MountainFeature.

    private final Supplier<Feature> factory;

    FeatureType(Supplier<Feature> factory) {
        this.factory = factory;
    }

    public Feature create() {
        return factory.get();
    }
}
