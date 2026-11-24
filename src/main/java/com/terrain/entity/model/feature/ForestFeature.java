package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.TileType;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;

public class ForestFeature extends Feature {
    private final NumberParameter radius = number("Radius", 50, 1, 500);
    private final NumberParameter treeDensity = number("Tree Density", 0.5, 0.0, 1.0);
    private final NumberParameter smoothness = number("Smoothness", 0.5, 0.0, 1.0);
    private final CoordinateParameter location = coordinate("Location", 0, 0);

    @Override
    public String getName() {
        return "Forest";
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        double r = radius.getValue();
        double td = treeDensity.getValue();
        double sm = smoothness.getValue();
        double x = location.getX();
        double y = location.getY();
        int width = map.getWidth();
        int height = map.getHeight();

        for (int mapX = 0; mapX < width; mapX++) {
            for (int mapY = 0; mapY < height; mapY++) {
                double distanceFromCenter = getDistance(x, y, mapX, mapY);

                // if the distance from the center is greater than the radius, skip tile
                if (distanceFromCenter > r) {
                    continue;
                }

                // adjust tree density based on distance from center and smoothness
                double tileTreeDensity = td; // start with base tree density
                if (distanceFromCenter > r * sm) {
                    // adjust per-tile density based on distance from center and smoothness
                    tileTreeDensity *= (r - distanceFromCenter) / (r * (1 - sm));
                }

                // randomly decide whether to place a tree based on the adjusted tree density
                if (tileTreeDensity > 0.5) {
                    map.getTile(mapX, mapY).setType(TileType.FOREST); // set type to forest
                }
            }
        }
    }
}
