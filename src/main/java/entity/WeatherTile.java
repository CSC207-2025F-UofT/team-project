package entity;

public class WeatherTile {
    private double zoom;
    private TileCoordinate coordinates;
    private java.time.Instant timestamp;

    public WeatherTile(double zoom, TileCoordinate coordinates){
        this.zoom = zoom;
        this.coordinates = coordinates;
    }

    public double getZoom() { return zoom; }

    public TileCoordinate getCoordinates() {
        return coordinates;
    }

    public String generateKey(){
        return coordinates.x+","+coordinates.y+","+zoom+","+timestamp;
    }
}
