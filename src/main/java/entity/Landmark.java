package entity;

public class Landmark {
    private final String landmarkId;
    private final String landmarkName;
    private final String landmarkType;
    private final Location location;
    private final LandmarkInfo landmarkInfo;
    private final int totalVisitCount;

    public Landmark(String landmarkId, String landmarkName, String landmarkType, Location location, LandmarkInfo landmarkInfo) {
        this.landmarkId = landmarkId;
        this.landmarkName = landmarkName;
        this.landmarkType = landmarkType;
        this.location = location;
        this.landmarkInfo = landmarkInfo;
        this.totalVisitCount = 0;
    }

}
