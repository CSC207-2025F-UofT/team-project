package use_case.ViewRecentFlights;

import entity.AirlineFlight;
import java.util.List;


public class ViewRecentFlightsOutputData {

    private final List<AirlineFlight> flights;

    public ViewRecentFlightsOutputData(List<AirlineFlight> flights) {
        this.flights = flights;
    }

    public List<AirlineFlight> getFlights() {
        return flights;
    }
}
