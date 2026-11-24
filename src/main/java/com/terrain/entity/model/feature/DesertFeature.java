package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.TileType;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import de.articdive.jnoise.pipeline.JNoise;

public class DesertFeature extends Feature implements BlobShaped {
    private final BlobParameters blobParameters = createBlobParameters(); // implements BlobShaped so needs these too
    private final CoordinateParameter location = coordinate("Location", 128, 128);

    @Override
    public String getName() {
        return "Desert";
    }

    @Override
    public BlobParameters getBlobParameters() {
        return blobParameters;
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        double radius = blobParameters.radius.getValue();
        double noiseAmplitude = blobParameters.noiseAmplitude.getValue() * 0.5;
        double noiseScale = blobParameters.noiseScale.getValue();
        JNoise noise = noise(random);

        for (int x = 0; x < map.getWidth(); x++) {
            for (int y = 0; y < map.getHeight(); y++) {
                if (isInBlob(x, y, location.getX(), location.getY(), radius, noiseAmplitude, noiseScale, noise)) {
                    map.getTile(x, y).setType(TileType.SAND);
                }
            }
        }
    }
}