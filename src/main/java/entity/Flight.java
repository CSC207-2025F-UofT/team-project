package entity;

public class Flight {
    public final String depAirport;
    public final String depTime;
    public final String arrAirport;
    public final String arrTime;
    public final double priceTotal;
    public final String currency;
    public final String duration;
    public final String airline;
    public final String aircraft;

    public Flight(String depAirport, String deptime,
                  String arrAirport, String arrTime,
                  double priceTotal, String currency,
                  String duration, String airline, String aircraft) {
        this.depAirport = depAirport;
        this.depTime = deptime;
        this.arrAirport = arrAirport;
        this.arrTime = arrTime;
        this.priceTotal = priceTotal;
        this.currency = currency;
        this.duration = duration;
        this.airline = airline;
        this.aircraft = aircraft;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s, %.2f, %s, %s, %s, %s",
                depAirport, depTime, arrAirport, arrTime, priceTotal,
                currency, duration, airline, aircraft);
    }
}
