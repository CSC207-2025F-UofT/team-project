package use_case;

import java.util.List;

import entity.Trip;

public interface TripAccessInterface {
    Trip getTripById(String id);
    List<Trip> getTrips();

    void writeTrip(Trip trip);
    void writeTrips(List<Trip> trips);
}
