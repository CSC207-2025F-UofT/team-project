package entity;

public class Flight {
    private String flightNumber;
    private String departureAirport;
    private String arrivalAirport;
    private String status;
    private String departureTime;
    private String arrivalTime;

    public Flight(String flightNumber, String departureAirport, String arrivalAirport,
                  String status, String departureTime, String arrivalTime) {
        this.flightNumber = flightNumber;
        this.departureAirport = departureAirport;
        this.arrivalAirport = arrivalAirport;
        this.status = status;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
