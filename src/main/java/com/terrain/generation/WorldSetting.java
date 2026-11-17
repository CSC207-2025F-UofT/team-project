package com.terrain.generation;

import com.terrain.model.feature.parameter.BooleanParameter;
import com.terrain.model.feature.parameter.NumberParameter;
import com.terrain.model.feature.parameter.ParameterSet;

// All the non-feature settings for a map; also contains default values for the map generation.
// Parameters are shown in the UI in the order they are declared here.
public class WorldSetting extends ParameterSet {
    public final NumberParameter height = number("Height", 256, 1, 4096);
    public final NumberParameter width = number("Width", 256, 1, 4096);
    public final NumberParameter seaLevel = number("Sea level", 0, 0, 1000);
    public final BooleanParameter islandMode = bool("Island mode", false);
    // Island mode would be a setting that makes the map generate as an island, with water around the edges.

    // Whole-tile dimensions for building the map.
    public int mapWidth() {
        return (int) width.getValue();
    }

    public int mapHeight() {
        return (int) height.getValue();
    }

    // Things such as temperature range, moisture range, etc. can be added here as needed.
}
