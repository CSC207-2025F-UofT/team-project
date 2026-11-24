package com.terrain.entity.model.feature;

import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;

import java.util.Random;

// A feature with per-instance blob parameters. Tile generation will be defined separately.
public interface BlobShaped {
    BlobParameters getBlobParameters();

    // returns true if tile is is in blob
    default boolean isInBlob(double x, double y, double centerX, double centerY, double radius, double noiseAmplitude, double noiseScale, JNoise noise) {
        double scale = 1 / noiseScale;
        double noiseValue = noiseAmplitude * noise.evaluateNoise(x * scale, y * scale);
        double dx = x - centerX;
        double dy = y - centerY;

        return noiseValue + cone(dx, dy, radius) > 0; // noise - cone > 0 means inside the blob
    }

    // simple perlin noise map
    default JNoise noise(Random random) {
        return JNoise.newBuilder()
            .perlin(random.nextInt(),
                    Interpolation.COSINE,
                    FadeFunction.QUINTIC_POLY
            ).build();
    }

    // cone with slope 1, center at (0, 0) and the z-slice through z=0 has radius r
    private static double cone(double x, double y, double r) {
        return - Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2)) + r;
    }

}
