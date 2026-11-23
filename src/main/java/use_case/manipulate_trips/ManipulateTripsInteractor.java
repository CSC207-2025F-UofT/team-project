package use_case.manipulate_trips;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import entity.LocationInfo;
import entity.Photo;
import entity.Trip;
import use_case.TripAccessInterface;
import use_case.TripAccessException;
import use_case.PhotoAccessInterface;
import use_case.PhotoAccessException;

/** Browsing and editing trips: list, view one, split, merge. */
public class ManipulateTripsInteractor implements ManipulateTripsInputBoundary {
    static final String MISSING_NAME = "Enter a name for the new trip.";
    static final String PHOTO_NOT_IN_TRIP = "That photo isn't in this trip.";
    static final String SPLIT_AT_FIRST = 
            "Pick a photo after the first one. Splitting at the first photo would leave an empty trip.";
    static final String TOO_FEW_TRIPS = "Select at least two trips to merge.";

    private static final Comparator<TripSummary> NEWEST_FIRST = Comparator.comparing(
            TripSummary::getStartTime, Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder()));
    private static final Comparator<Trip> TRIPS_NEWEST_FIRST = Comparator.comparing(
            Trip::getStartTime, Comparator.nullsLast(Comparator.<LocalDateTime>reverseOrder()));
    private static final Comparator<Trip> TRIPS_EARLIEST_FIRST = Comparator.comparing(
            Trip::getStartTime, Comparator.nullsLast(Comparator.<LocalDateTime>naturalOrder()));
    
    private final TripAccessInterface tripAccess;
    private final PhotoAccessInterface photoAccess;
    private final ManipulateTripsOutputBoundary presenter;
    private final Supplier<String> newTripIds;

    /** @param newTripIds makes a fresh unique id for each trip created by a split*/
    public ManipulateTripsInteractor(TripAccessInterface tripAccess, PhotoAccessInterface photoAccess,
                               ManipulateTripsOutputBoundary presenter, Supplier<String> newTripIds) {
        this.tripAccess = tripAccess;
        this.photoAccess = photoAccess;
        this.presenter = presenter;
        this.newTripIds = newTripIds;
    }

    @Override 
    public void listTrips() {
        List<TripSummary> summaries = new ArrayList<>();
        try {
            for (Trip trip : tripAccess.getTrips()) {
                summaries.add(summarize(trip));
            }
        } catch (TripAccessException | PhotoAccessException e ) {
            presenter.prepareTripListFailView("Couldn't load trips: " + e.getMessage());
            return;
        }
        summaries.sort(NEWEST_FIRST);
        presenter.prepareTripListView(new ListTripsOutputData(summaries));
    }

    private TripSummary summarize(Trip trip) {
        List<Photo> photos = chronological(photoAccess.getPhotosByTripId(trip.getId()));

        String coverPhotoId = photos.isEmpty() ? null : photos.get(0).getId();

        return new TripSummary(trip.getId(), trip.getName(), trip.getStartTime(), trip.getEndTime(), photos.size(), mostCommonLocation(photos), coverPhotoId);
    }

    /** The most frequent "City, Country" among the photos. Earliest photo wins for ties. */
    private static String mostCommonLocation(List<Photo> photos) {
        Map<String, Long> counts = photos.stream()
                .map(ManipulateTripsInteractor::labelOf)
                .filter(label -> label != null)
                .collect(Collectors.groupingBy(Function.identity(), LinkedHashMap::new, Collectors.counting()));

        return counts.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse(null);
    }

    private static String labelOf(Photo photo) {
        if (photo.getLocation() == null || photo.getLocation().getInfo() == null) {
            return null;
        }
        LocationInfo info = photo.getLocation().getInfo();
        String label = Stream.of(info.getCity(), info.getCountry())
                .filter(s -> s != null && !s.isBlank())
                .collect(Collectors.joining(", "));
        return label.isEmpty() ? null : label;
    }


    // Viewing Trips
    @Override 
    public void viewTrip(ViewTripInputData inputData) {
        TripDetailsOutputData details;
        try {
            details = detailsOf(inputData.getTripId());
        } catch (TripAccessException | PhotoAccessException e) {
            presenter.prepareTripDetailsFailView("Couldn't open trip: " + e.getMessage());
            return;
        }
        presenter.prepareTripDetailsView(details);
    }

    /** Reads one trip for the details screen. Used by view, split & merge. */
    private TripDetailsOutputData detailsOf(String tripId) {
        Trip trip = tripAccess.getTripById(tripId);
        
        List<PhotoDetails> photos = chronological(photoAccess.getPhotosByTripId(tripId)).stream()
                .map(photo -> new PhotoDetails(photo.getId(), photo.getTime()))
                .collect(Collectors.toList());

        List<TripReference> otherTrips = tripAccess.getTrips().stream()
                .filter(other -> !other.getId().equals(tripId))
                .sorted(TRIPS_NEWEST_FIRST)
                .map(other -> new TripReference(other.getId(), other.getName(),
                        other.getStartTime(), other.getEndTime()))
                .collect(Collectors.toList());

        return new TripDetailsOutputData(trip.getId(), trip.getName(), trip.getStartTime(), trip.getEndTime(),
                photos, otherTrips);
    }

    // Splitting trips
    @Override 
    public void splitTrip(SplitTripInputData inputData) {
        String newName = inputData.getNewTripName() == null ? "" : inputData.getNewTripName().strip();
        if (newName.isEmpty()) {
            presenter.prepareSplitFailView(MISSING_NAME);
            return;
        }

        SplitTripOutputData outputData;
        try {
            Trip original = tripAccess.getTripById(inputData.getTripId());
            List<Photo> photos = chronological(photoAccess.getPhotosByTripId(original.getId()));

            int splitIndex = indexOf(photos, inputData.getFirstPhotoId());
            if (splitIndex < 0) {
                presenter.prepareSplitFailView(PHOTO_NOT_IN_TRIP);
                return;
            }
            if (splitIndex == 0) {
                presenter.prepareSplitFailView(SPLIT_AT_FIRST);
                return;
            }

            // Rebuild both trips through addPhoto so start & end times are recomputed.
            Trip before = new Trip(original.getId(), original.getName(), null, null);
            photos.subList(0, splitIndex).forEach(before::addPhoto);

            Trip after = new Trip(newTripIds.get(), newName, null, null);
            photos.subList(splitIndex, photos.size()).forEach(after::addPhoto);

            tripAccess.writeTrips(List.of(before, after));
            outputData = new SplitTripOutputData(detailsOf(before.getId()), after.getId(), after.getName());
        } catch (TripAccessException | PhotoAccessException e) {
            presenter.prepareSplitFailView("Couldn't split trip: " + e.getMessage());
            return;
        }
        presenter.prepareSplitSuccessView(outputData);
    }

    public static int indexOf(List<Photo> photos, String photoId) {
        for (int i = 0; i < photos.size(); i++) {
            if (photos.get(i).getId().equals(photoId)) {
                return i;
            }
        }
        return -1;
    }


    // Merge Trips
    @Override 
    public void mergeTrips(MergeTripsInputData inputData) {
        List<String> tripIds = new ArrayList<>(new LinkedHashSet<>(inputData.getTripIds())); // drop dupes
        tripIds.removeIf(Objects::isNull);
        if (tripIds.size() < 2) {
            presenter.prepareMergeFailView(TOO_FEW_TRIPS);
            return;
        }

        MergeTripsOutputData outputData;
        try {
            List<Trip> trips = new ArrayList<>();
            Map<String, Photo> photosById = new LinkedHashMap<>();
            for (String id : tripIds) {
                trips.add(tripAccess.getTripById(id));
                photoAccess.getPhotosByTripId(id).forEach(photo -> photosById.putIfAbsent(photo.getId(), photo));
            }

            // Earliest trip survives, keeping its id & name.
            Trip earliest = trips.stream().min(TRIPS_EARLIEST_FIRST).orElseThrow();
            Trip merged = new Trip(earliest.getId(), earliest.getName(), null, null);
            chronological(new ArrayList<>(photosById.values())).forEach(merged::addPhoto);

            tripAccess.writeTrip(merged);
            for (Trip trip : trips) {
                if (!trip.getId().equals(merged.getId())) {
                    tripAccess.deleteTripById(trip.getId());
                }
            }
            outputData = new MergeTripsOutputData(detailsOf(merged.getId()), trips.size());
        } catch (TripAccessException | PhotoAccessException e) {
            presenter.prepareMergeFailView("Couldn't merge trips: " + e.getMessage());
            return;
        }
        presenter.prepareMergeSuccessView(outputData);
    }

    /** A sorted copy: oldest first, photos without a time last. */
    private static List<Photo> chronological(List<Photo> photos) {
        List<Photo> sorted = new ArrayList<>(photos);
        Collections.sort(sorted);
        return sorted;
    }
}