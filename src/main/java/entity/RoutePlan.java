package entity;

import java.util.List;

public class RoutePlan {
    private final Location startLocation;
    private List<Location> waypoints;
    private final Location endLocation;
    private int estimatedTime;
    private int estimatedDistance;
    private String polylineEncoded;
    private List<RouteStep> steps;

    public RoutePlan(Location startLocation, Location endLocation) {
        this.startLocation = startLocation;
        this.endLocation = endLocation;
    }

}
