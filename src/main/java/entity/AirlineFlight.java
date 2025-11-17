package entity;

public class AirlineFlight extends Flight{

    private final String airline;

    public AirlineFlight(String flightNumber, String departureAirport, String arrivalAirport,
                         String status, String departureTime, String arrivalTime,
                         String airline) {
        super(flightNumber, departureAirport, arrivalAirport, status, departureTime, arrivalTime);
        this.airline = airline;
    }

    public String getAirline() {
        return airline;
    }
}
