package data_access;

import entity.Photo;
import entity.Trip;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Access to the trip entity.
 */
public interface TripDataAccessInterface {
    public Optional<Trip> getTrip(String tripId);
    public List<Trip> getAllTrips();

    public void saveTrip(Trip trip);

    public void deleteTrip(String tripId);

    public List<Photo> getTripPhotos(String tripId);
}