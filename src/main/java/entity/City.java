package entity;
// TODO: do we need to include distance and direction to AnswerCity in this class?

public class City {
    private final String name;
    private final double lat;
    private final double lon;

    public boolean equals(City city) {
        return city.getName().equals(name);
    }

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
}