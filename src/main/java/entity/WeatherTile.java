package entity;

public class WeatherTile {
    private TileCoords coordinates;
    private java.time.Instant timestamp;

    public WeatherTile(TileCoords coordinates, java.time.Instant timestamp) {
        this.coordinates = coordinates;
        this.timestamp = timestamp;
    }

    public double getZoom() { return zoom; }

    public TileCoords getCoordinates() {
        return coordinates;
    }

    public String generateKey(){
        return coordinates.x+","+coordinates.y+","+zoom+","+timestamp;
    }
}
