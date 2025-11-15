package interface_adapter.flight_results;

import entity.Flight;
import java.util.List;
import java.util.ArrayList;

public class FlightResultsState {
    private List<Flight> flights = new ArrayList<>();
    private String error = null;

    public FlightResultsState(FlightResultsState copy) {
        flights = copy.flights;
        error = copy.error;
    }

    public FlightResultsState() {}

    public List<Flight> getFlights() {
        return flights;
    }

    public void setFlights(List<Flight> flights) {
        this.flights = flights;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}