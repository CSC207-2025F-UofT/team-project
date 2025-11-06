package entity;

public class WeatherTile {
    private double zoom;
    private TileCoordinate coordinates;

    public double getZoom() { return zoom; }

    public TileCoordinate getCoordinates() {
        return coordinates;
    }

    public WeatherTile(double zoom, TileCoordinate coordinates){
        this.zoom = zoom;
        this.coordinates = coordinates;
    }
}
