package com.terrain.entity.model.feature;

import com.terrain.entity.model.feature.parameter.NumberParameter;

// this class is a simple container for the parameters that define a blob-shaped feature
public final class BlobParameters {
    public final NumberParameter radius;
    public final NumberParameter noiseAmplitude;
    public final NumberParameter noiseScale;

    BlobParameters(NumberParameter radius, NumberParameter noiseAmplitude, NumberParameter noiseScale) {
        this.radius = radius;
        this.noiseAmplitude = noiseAmplitude;
        this.noiseScale = noiseScale;
    }
}
