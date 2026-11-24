package com.terrain.entity.model.feature;

import java.util.Random;

import com.terrain.entity.model.TerrainMap;
import com.terrain.entity.model.feature.parameter.CoordinateParameter;
import com.terrain.entity.model.feature.parameter.NumberParameter;

public class MountainFeature extends Feature {
    private final NumberParameter height = number("Height", 100, 10, 1000);
    private final NumberParameter radius = number("Radius", 50, 1, 500);
    private final CoordinateParameter peakLocation = coordinate("Peak location", 0, 0);

    @Override
    public String getName() {
        return "Mountain";
    }

    @Override
    public void apply(TerrainMap map, Random random) {
        // this is how the parameters are accessed
        double h = height.getValue();          // NumberParameter -> double
        double r = radius.getValue();
        double px = peakLocation.getX();       // CoordinateParameter -> getX()/getY()
        double py = peakLocation.getY();       // already clamped to the map size

        // TODO: raise elevation around the peak (done)
        int width = map.getWidth();
        int height = map.getHeight();
        
        for (int mapX = 0; mapX < width; mapX++) {
            for (int mapY = 0; mapY < height; mapY++) {
                double distanceFromCenter = getDistance(px, py, mapX, mapY);

                if (distanceFromCenter < r) {
                    double interpolatedElevation = (1 - distanceFromCenter / r) * h;

                    double elevation = Math.max(map.getTile(mapX, mapY).getElevation(), interpolatedElevation);

                    map.getTile(mapX, mapY).setElevation(elevation);
                }
            }
        }
    }

    private static double getDistance(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
        return distance;
    }
}
