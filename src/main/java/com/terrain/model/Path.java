package com.terrain.model;

import com.terrain.model.feature.parameter.CoordinateParameter;
import java.util.*;


public class Path {
    private final int[] start;
    private final int[] end;
    private int width;

    public Path(int[] start, int[] end, int width) {
        this.start = start;
        this.end = end;
        this.width = width;
    }

    public List<int[]> generate(int seed, TerrainMap map, double pull, double wobble) {
        Random random = new Random(seed);
        double x = start[0], y = start[1];
        double horizon = Math.toRadians(50); // checks whether the path passes an "event horizon" by which point it can't turn back to reach the end
        double baseAngle = Math.atan2(end[1]-y, end[0]-x); // angle between start and end
        double currentAngle = baseAngle;
        ArrayList<int[]> path = new ArrayList<>();

        while (Math.abs(end[0] - x) > 1.5 || Math.abs(end[1] - y) > 1.5) {
            double angleTo = Math.atan2(end[1] - y, end[0] - x); // angle between current position in path and end

            double chosen = normalize(angleTo - currentAngle);
            for (int tries = 0; tries < 50; tries++) {
                double dangle = pull * normalize(angleTo - currentAngle) + random.nextGaussian() * wobble;
                if (Math.abs(normalize(currentAngle+dangle-baseAngle)) <= Math.PI/2 &&
                    endReachable(x + Math.cos(currentAngle+dangle), y + Math.sin(currentAngle+dangle), baseAngle, horizon)) {
                    chosen = dangle;
                    break;
                }
            }
            currentAngle += chosen;

            x += Math.cos(currentAngle);
            y += Math.sin(currentAngle);

            int [] tile = {(int) Math.round(x), (int) Math.round(y)};
            path.add(tile);
        }
        return path;
    }

    private static double normalize(double a) {
        return Math.atan2(Math.sin(a), Math.cos(a));
    }

    private boolean endReachable(double px, double py, double baseAngle, double horizon) {
        double dx = end[0] - px, dy = end[1] - py;
        double forward =  dx * Math.cos(baseAngle) + dy * Math.sin(baseAngle);
        double lateral = -dx * Math.sin(baseAngle) + dy * Math.cos(baseAngle);
        return forward > 0 && Math.abs(lateral) <= forward * Math.tan(horizon);
    }
}


