package com.terrain.model.feature;

import com.terrain.model.TerrainMap;
import com.terrain.model.feature.parameter.CoordinateParameter;
import com.terrain.model.feature.parameter.NumberParameter;

import java.util.Random;

public class RiverFeature extends Feature {
    private final CoordinateParameter start = coordinate("Start", 0, 0);
    private final CoordinateParameter end = coordinate("End", 100, 100);
    private final NumberParameter width = number("Width", 5, 1, 20);
    private final NumberParameter meander = number("Meander", 5, 1, 20);
    // Considering adding a "hit these points" parameter instead of meander, but that would require a list of
    // coordinates, which is more complex than the current parameter types allow for. Perhaps if we get bored.
    // Could also replace start and end with a list of coordinates? Like, user just chooses a bunch of points and the
    // river goes through them?

    @Override
    public String getName() {
        return "River";
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        // TODO: carve a meandering path from start to end.
    }
}
