package entity;

public class WeatherTile {
    private final TileCoords coordinates;
    private final java.time.Instant timestamp;
    private final WeatherType weatherType;

    public WeatherTile(TileCoords coordinates, java.time.Instant timestamp, WeatherType weatherType) {
        this.coordinates = coordinates;
        this.timestamp = timestamp;
        this.weatherType = weatherType;
    }

    public java.time.Instant getTimestamp() { return timestamp; }

    public TileCoords getCoordinates() {
        return coordinates;
    }

    public WeatherType getWeatherType() {
        return weatherType;
    }

    public String generateKey(){
        return coordinates.x+","+coordinates.y+","+zoom+","+timestamp;
    }
}
