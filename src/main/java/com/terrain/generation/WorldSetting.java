package com.terrain.generation;

import com.terrain.entity.model.feature.parameter.BooleanParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;
import com.terrain.entity.model.feature.parameter.ParameterSet;

// All the non-feature settings for a map; also contains default values for the map generation.
// Parameters are shown in the UI in the order they are declared here.
public class WorldSetting extends ParameterSet {
    public final NumberParameter height = number("Height", 256, 1, 4096);
    public final NumberParameter width = number("Width", 256, 1, 4096);
    public final NumberParameter seaLevel = number("Sea level", 50, 0, 1000);
    public final NumberParameter landLevel = number("Land level", 100, 0, 1000);
    public final NumberParameter noiseAmplitude = number("Noise Amplitude", 40, 0, 100);
    public final NumberParameter noiseScale = number("Noise Scale", 30, 0.01, 100);
    public final BooleanParameter islandMode = bool("Island mode", true);
    public final IslandSettings island = new IslandSettings();

    public static final class IslandSettings extends ParameterSet {
        public final NumberParameter radius = number("Island radius", 80, 1, 1000);
        public final NumberParameter cornerRoundness = number("Island corner roundness", 4, 1, 10);
        public final NumberParameter slope = number("Island slope", 3, 0, 10);
        public final NumberParameter height = number("Island height", 50, 0, 1000);
    }

    // Whole-tile dimensions for building the map.
    public int mapWidth() {
        return (int) width.getValue();
    }

    public int mapHeight() {
        return (int) height.getValue();
    }

    // Things such as temperature range, moisture range, etc. can be added here as needed.
}
