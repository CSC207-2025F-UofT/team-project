package entity;

public class Viewport {
    private Location centre;
    private float zoomLevel;

    public Viewport(Location centre, float zoomLevel) {
        this.centre = centre;
        this.zoomLevel = zoomLevel;
    }

    public BoundingBox calculateBBox(){
        return null;
    }
    public Location getCentre() {
        return centre;
    }

    public float getZoomLevel() {
        return zoomLevel;
    }
}
