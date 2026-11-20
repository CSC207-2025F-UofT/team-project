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
public class InMemoryTripDataAcessObject implements TripAccessInterface, PhotoAccessInterface {
    private final Map<String, Trip> trips = new LinkedHashMap<>();
    private final Map<String, Photo> photos = new HashMap<>();
    
    @Override 
    public Trip getTripById(String id) {
        Trip trip = trips.get(id);
        if (trip == null) throw new TripAccessException("No trip with id " + id);
        return trip;
    }

    @Override
    public List<Trip> getTrips() {
        return new ArrayList<>(trips.values());
    }

    @Override 
    public void writeTrip(Trip trip) {
        trips.put(trip.getId(), trip);
    }

    @Override 
    public void writeTrips(List<Trip> toWrite) {
        toWrite.forEach(this::writeTrip);
    }

    @Override
    public Photo getPhotoById(String id) {
        Photo photo = photos.get(id);
        if (photo == null) throw new PhotoAccessException("No photo with id " + id);
        return photo;
    }

    @Override
    public List<Photo> getPhotos() {
        return new ArrayList<>(photos.values());
    }

    @Override
    public void writePhoto(Photo photo) {
        photos.put(photo.getId(), photo);
    }

    @Override
    public void writePhotos(List<Photo> toWrite) {
        toWrite.forEach(this::writePhoto);
    }

    @Override
    public List<Photo> getPhotosByTripId(String tripId) {
        Trip trip = trips.get(tripId);
        if (trip == null) {
            return new ArrayList<>();
        }
        return trip.getPhotoIds().stream()
                .map(photos::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    // TODO: add this to an interface (tripAccessInterface?)
    public void deleteTrip(String tripId) {
        trips.remove(tripId);
    }
}
