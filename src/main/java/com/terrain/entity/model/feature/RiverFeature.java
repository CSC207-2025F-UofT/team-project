package com.terrain.entity.model.feature;

import java.util.List;
import java.util.Random;

import com.terrain.entity.model.Path;
import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.TileType;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;

public class RiverFeature extends Feature {
    private final CoordinateParameter start = coordinate("Start", 0, 0);
    private final CoordinateParameter end = coordinate("End", 100, 100);
    private final NumberParameter width = number("Width", 5, 1, 20);
    private final NumberParameter pull = number("Pull", 0.5, 0, 1);
    private final NumberParameter wobble = number("Wobble (radians)", 1.57, 0, 6.28);

    @Override
    public String getName() {
        return "River";
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        int startX = (int) start.getX();
        int startY = (int) start.getY();
        int endX = (int) end.getX();
        int endY = (int) end.getY();
        int wd = (int) width.getValue();
        double p = pull.getValue();
        double wb = wobble.getValue();
        int[] start = {startX, startY};
        int[] end = {endX, endY};
        Path path = new Path(start, end, wd);
        List<int[]> riverPath = path.generate(0, map, p, wb);

        for (int[] coordinate : riverPath) {
            map.getTile(coordinate[0], coordinate[1]).setType(TileType.WATER);
        }
    }
}
