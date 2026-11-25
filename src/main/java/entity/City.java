package entity;
// TODO: do we need to include distance and direction to AnswerCity in this class?

import java.util.Objects;

public class City {
    private final String name;
    private final double lat;
    private final double lon;

    public City(
        String name,
        double lat,
        double lon
    ) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return this.name;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    @Override
    public boolean equals(Object o){
        if (this == o) {
            return true;
        }
        if (!(o instanceof City)) {
            return false;
        }
        City other = (City) o;
        return Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}