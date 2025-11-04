package entity;

public class BoundingBox {
    private final Location topLeft;
    private final Location bottomRight;
    public BoundingBox(Location topLeft, Location bottomRight) {
        this.topLeft = topLeft;
        this.bottomRight = bottomRight;
    }
    public boolean locationInBBox(Location location) {
        boolean latitudeValid = location.getLatitude() <= topLeft.getLatitude()
                && location.getLatitude() >= bottomRight.getLatitude();
        boolean longitudeValid = location.getLongitude() >= topLeft.getLongitude()
                && location.getLongitude() <= bottomRight.getLongitude();
        return latitudeValid && longitudeValid;
    }
}
