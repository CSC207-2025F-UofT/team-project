package use_case.manipulate_trips;

import java.util.List;

public class ListTripsOutputData {
    private final List<TripSummary> trips;

    public ListTripsOutputData(List<TripSummary> trips) {
        this.trips = List.copyOf(trips);
    }

    /** Newest first. May be empty. */
    public List<TripSummary> getTrips() {
        return trips;
    }
}