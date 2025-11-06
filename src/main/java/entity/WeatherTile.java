package entity;

public class WeatherTile {
    private double zoom;
    private TileCoords coordinates;
    private java.time.Instant timestamp;

    public WeatherTile(double zoom, TileCoords coordinates){
        this.zoom = zoom;
        this.coordinates = coordinates;
    }

    public double getZoom() { return zoom; }

    public TileCoords getCoordinates() {
        return coordinates;
    }

    public String generateKey(){
        return coordinates.x+","+coordinates.y+","+zoom+","+timestamp;
    }
}
