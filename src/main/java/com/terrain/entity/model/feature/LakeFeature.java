package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.TileType;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;

public class LakeFeature extends Feature {
    private final NumberParameter radius = number("Radius", 50, 1, 500);
    private final NumberParameter depth = number("Depth", 10, 1, 100); // Tentative boundaries
    private final NumberParameter smoothness = number("Smoothness", 0.5, 0.0, 1.0);  // How quickly the the depth increases as it gets closer to the shore
    private final CoordinateParameter location = coordinate("Location", 0, 0);

    @Override
    public String getName() {
        return "Lake";
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        double r = radius.getValue();
        double d = depth.getValue();
        double sm = smoothness.getValue();
        double x = location.getX();
        double y = location.getY();

        int width = map.getWidth();
        int height = map.getHeight();
        
        for (int mapX = 0; mapX < width; mapX++) {
            for (int mapY = 0; mapY < height; mapY++) {
                double distanceFromCenter = getDistance(x, y, mapX, mapY);

                // Reduce water depth if point is far away from the center; based on smoothness
                // NOTE: Completely untested lol!!!! wish me luck
                if (distanceFromCenter > r * sm) {
                    d = d / (r * (sm - 1)) * distanceFromCenter + d; // literally y = mx + c
                }

                if (distanceFromCenter < r) {
                    map.getTile(mapX, mapY).setElevation(map.getTile(mapX, mapY).getElevation() - d);
                    map.getTile(mapX, mapY).setType(TileType.WATER);
                }
            }
        }
    }

    private static double getDistance(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return distance;
    }
}