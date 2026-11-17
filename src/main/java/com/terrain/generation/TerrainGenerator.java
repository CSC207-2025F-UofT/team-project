package com.terrain.generation;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.feature.Feature;

public class TerrainGenerator {
    // Same settings and seed always produce the same map.
    public TerrainMap generate(GenerationSettings settings) {
        TerrainMap map = new TerrainMap(settings.world.mapWidth(), settings.world.mapHeight());

        long seed = settings.seed;
        int w = settings.world.mapWidth();
        int h = settings.world.mapHeight();
        double seaLevel = settings.world.seaLevel.getValue();
        boolean islandMode = settings.world.islandMode.getValue();

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
