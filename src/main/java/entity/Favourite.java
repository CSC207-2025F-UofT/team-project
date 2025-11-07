package entity;

public class Favourite {

    private final String userId;
    private final String flightNumber;

    public Favourite(String userId, String flightNumber) {
        this.userId = userId;
        this.flightNumber = flightNumber;
    }

    public String getUserId() {
        return userId;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Favourite favourite = (Favourite) obj;
        return userId.equals(favourite.userId) &&
                flightNumber.equals(favourite.flightNumber);
    }

    @Override
    public int hashCode() {
        return userId.hashCode() + flightNumber.hashCode();
    }
}
