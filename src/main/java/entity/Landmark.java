package entity;

public class Landmark {
    private final String landmarkName;
    private final Location location;
    private final LandmarkInfo landmarkInfo;
    private final int totalVisitCount;

    public Landmark(String landmarkName, Location location, LandmarkInfo landmarkInfo, int totalVisitCount) {
        this.landmarkName = landmarkName;
        this.location = location;
        this.landmarkInfo = landmarkInfo;
        this.totalVisitCount = totalVisitCount;
    }

    public String getLandmarkName() {
        return landmarkName;
    }

    public Location getLocation() {
        return location;
    }

    public LandmarkInfo getLandmarkInfo() {
        return landmarkInfo;
    }

    public int getTotalVisitCount() {
        return totalVisitCount;
    }

}
