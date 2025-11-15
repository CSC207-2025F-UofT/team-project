package use_case.sort_flights;

import entity.Flight;
import java.util.List;

public class SortFlightsInputData {
    private final List<Flight> flights;
    private final String sortType; // e.g., "PRICE" or "DURATION"

    public SortFlightsInputData(List<Flight> flights, String sortType) {
        this.flights = flights;
        this.sortType = sortType;
    }

    public List<Flight> getFlights() {
        return flights;
    }

    public String getSortType() {
        return sortType;
    }
}
