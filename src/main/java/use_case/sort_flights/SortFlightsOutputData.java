package use_case.sort_flights;

import entity.Flight;
import java.util.List;

public class SortFlightsOutputData {
    private final List<Flight> sortedFlights;

    public SortFlightsOutputData(List<Flight> sortedFlights) {
        this.sortedFlights = sortedFlights;
    }

    public List<Flight> getSortedFlights() {
        return sortedFlights;
    }
}
