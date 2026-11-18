package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;

public class ForestFeature extends Feature {
    private final NumberParameter radius = number("Radius", 50, 1, 500);
    private final NumberParameter treeDensity = number("Tree Density", 0.5, 0.0, 1.0);
    private final NumberParameter smoothness = number("Smoothness", 0.5, 0.0, 1.0);  // How quickly the tree density decreases as it gets further from the radius; if you got a better name for this please tell me!
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
        
        // TODO: apply forest 

        int width = map.getWidth();
        int height = map.getHeight();
        
        for (int mapX = 0; mapX < width; mapX++) {
            for (int mapY = 0; mapY < height; mapY++) {
                double distanceFromCenter = getDistance(x, y, mapX, mapY);

                // Reduce tree density if point is far away from the center; based on smoothness
                if (distanceFromCenter > r * sm) {
                    td = td / (r * (sm - 1)) * distanceFromCenter + td; // literally y = mx + c
                }

                map.getTile(mapX, mapY).setTreeDensity(td);
            }
        }
    }

    private static double getDistance(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt(Math.pow(x1 - y1, 2) + Math.pow(x2 - y2, 2));
        return distance;
    }
}
