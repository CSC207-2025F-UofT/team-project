package data_access;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import entity.Photo;
import entity.Trip;
import use_case.PhotoAccessException;
import use_case.PhotoAccessInterface;
import use_case.TripAccessException;
import use_case.TripAccessInterface;

/**
 * In-memory storage for trips and photos. Implements each use case's
 * data-access interface as those use cases are added.
 */
public class InMemoryDataAccessObject implements PhotoAccessInterface, TripAccessInterface {
    private final Map<String, Photo> photos = new HashMap<>();
    private final Map<String, Trip> trips = new LinkedHashMap<>();
    private boolean photoFailMode = false;
    private boolean tripFailMode = false;

    @Override
    public Photo getPhotoById(String id) {
        if (photoFailMode) throw new PhotoAccessException("Photo access failed");

        Photo photo = photos.get(id);
        if (photo == null) throw new PhotoAccessException("No photo with id " + id);

        return photo;
    }

    @Override
    public List<Photo> getPhotos() {
        if (photoFailMode) throw new PhotoAccessException("Photo access failed");

        return new ArrayList<>(photos.values());
    }

    @Override
    public void writePhoto(Photo photo) {
        if (photoFailMode) throw new PhotoAccessException("Photo access failed");

        photos.put(photo.getId(), photo);
    }

    @Override
    public void writePhotos(List<Photo> toWrite) {
        if (photoFailMode) throw new PhotoAccessException("Photo access failed");

        toWrite.forEach(this::writePhoto);
    }

    @Override
    public List<Photo> getPhotosByTripId(String tripId) {
        if (photoFailMode) throw new PhotoAccessException("Photo access failed");

        Trip trip = trips.get(tripId);
        if (trip == null) return new ArrayList<>();

        return trip.getPhotoIds().stream()
            .map(photos::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }

    @Override 
    public Trip getTripById(String id) {
        if (tripFailMode) throw new TripAccessException("Trip access failed");

        Trip trip = trips.get(id);
        if (trip == null) throw new TripAccessException("No trip with id " + id);

        return trip;
    }

    @Override
    public List<Trip> getTrips() {
        if (tripFailMode) throw new TripAccessException("Trip access failed");

        return new ArrayList<>(trips.values());
    }

    @Override 
    public void writeTrip(Trip trip) {
        if (tripFailMode) throw new TripAccessException("Trip access failed");

        trips.put(trip.getId(), trip);
    }

    @Override 
    public void writeTrips(List<Trip> toWrite) {
        if (tripFailMode) throw new TripAccessException("Trip access failed");

        toWrite.forEach(this::writeTrip);
    }

    @Override
    public void deleteTripById(String tripId) {
        if (tripFailMode) throw new TripAccessException("Trip access failed");

        if (trips.remove(tripId) == null) {
            throw new TripAccessException("No trip with id " + tripId);
        }
    }

    public void setPhotoFailMode(boolean mode) {
        photoFailMode = mode;
    }

    public void setTripFailMode(boolean mode) {
        tripFailMode = mode;
    }
}
