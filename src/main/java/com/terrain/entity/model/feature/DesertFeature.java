package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.TileType;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;

public class DesertFeature extends Feature {
    private final NumberParameter radius = number("Radius", 50, 1, 500);
    private final CoordinateParameter location = coordinate("Location", 0, 0);

    @Override
    public String getName() {
        return "Desert";
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        double r = radius.getValue();
        double x = location.getX();
        double y = location.getY();

        int width = map.getWidth();
        int height = map.getHeight();
        
        for (int mapX = 0; mapX < width; mapX++) {
            for (int mapY = 0; mapY < height; mapY++) {
                double distanceFromCenter = Feature.getDistance(x, y, mapX, mapY);

                if (distanceFromCenter < r) {
                    map.getTile(mapX, mapY).setType(TileType.SAND);
                }
            }
        }
    }
}