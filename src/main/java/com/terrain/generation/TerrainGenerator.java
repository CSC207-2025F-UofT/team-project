package com.terrain.generation;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.Tile;
import com.terrain.entity.model.TileType;
import com.terrain.entity.model.feature.Feature;
import de.articdive.jnoise.core.api.functions.Interpolation;
import de.articdive.jnoise.generators.noise_parameters.fade_functions.FadeFunction;
import de.articdive.jnoise.pipeline.JNoise;

public class TerrainGenerator {
    // Same settings and seed always produce the same map.
    public TerrainMap generate(GenerationSettings settings) {
        TerrainMap map = new TerrainMap(settings.world.mapWidth(), settings.world.mapHeight());

        long seed = settings.seed;
        int w = settings.world.mapWidth();
        int h = settings.world.mapHeight();
        double seaLevel = settings.world.seaLevel.getValue();
        double landLevel = settings.world.landLevel.getValue();
        boolean islandMode = settings.world.islandMode.getValue();

        // adds some default noise to the map, so that it isn't just a flat plane.
        JNoise noise =JNoise.newBuilder()
                .perlin(3301,
                        Interpolation.COSINE,
                        FadeFunction.QUINTIC_POLY
                ).build();

        // for each tile...
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                // get the tile at this coordinate
                Tile tile = map.getTile(x, y);

                // generate noise value for this tile
                double scale = 0.05; // higher scale -> more noisy (scale = 1 is bad)
                double noiseValue = noise.evaluateNoise(x * scale, y * scale);
                double elevation = 10 * noiseValue + landLevel;


                tile.setElevation(elevation);

                // PLACEHOLDER CODE to make things interesting; should really just be all grass.
                if (x < w / 2) {
                    tile.setType(TileType.GRASS); // Default type, can be changed by features later.
                } else {
                    tile.setType(TileType.WATER);
                }
            }
        }

        for (int i = 0; i < settings.features.size(); i++) {
            Feature feature = settings.features.get(i);
            feature.setMapSize(settings.world.mapWidth(), settings.world.mapHeight());
            // Each feature gets its own Random derived from seed and index, so tweaking one feature
            // doesn't change the randomness other features see.

            // Multiplying by 31 is a cheap way to make the seeds more random. Makes it such that seed 100, feature 1 is
            // different from seed 101, feature 0, etc. 31 is just a prime number, helps with distribution. 127 works too
            feature.apply(map, new Random(settings.seed * 31 + i));
        }

        // TODO: apply world settings (sea level, island mode, etc.) to the map

        return map;
    }
}
