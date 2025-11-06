package entity;

public class WeatherTile {
    private final TileCoords coordinates;
    private final java.time.Instant timestamp;

    public WeatherTile(TileCoords coordinates, java.time.Instant timestamp) {
        this.coordinates = coordinates;
        this.timestamp = timestamp;
    }

    public java.time.Instant getTimestamp() { return timestamp; }

    public TileCoords getCoordinates() {
        return coordinates;
    }

    public String generateKey(){
        return coordinates.x+","+coordinates.y+","+zoom+","+timestamp;
    }
}
