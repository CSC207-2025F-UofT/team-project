package use_case.find_flight;

import entity.Flight;
import java.util.List;

public class FindFlightOutputData {
    private final List<Flight> flights;
    private final boolean useCaseFailed;

    public FindFlightOutputData(List<Flight> flights, boolean useCaseFailed) {
        this.flights = flights;
        this.useCaseFailed = useCaseFailed;
    }

    public List<Flight> getFlights() {
        return flights;
    }
}
