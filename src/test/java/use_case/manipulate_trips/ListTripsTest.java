package use_case.manipulate_trips;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import entity.Location;
import entity.LocationInfo;
import entity.Photo;
import entity.Trip;
import use_case.PhotoAccessException;
import use_case.PhotoAccessInterface;
import use_case.TripAccessException;
import use_case.TripAccessInterface;

import static org.junit.jupiter.api.Assertions.*;

/** Tests ManipulateTripsInteractor.listTrips(). */
class ListTripsTest {

    static class FakeDataAccess implements TripAccessInterface, PhotoAccessInterface {
        final List<Trip> trips = new ArrayList<>();
        final Map<String, Photo> photos = new HashMap<>();
        RuntimeException tripError;
        RuntimeException photoError;

        void addTrip(Trip trip, Photo... tripPhotos) {
            for (Photo p : tripPhotos) {
                photos.put(p.getId(), p);
                trip.addPhoto(p);
            }
            trips.add(trip);
        }

        @Override
        public List<Trip> getTrips() {
            if (tripError != null) {
                throw tripError;
            }
            return new ArrayList<>(trips);
        }

        @Override
        public List<Photo> getPhotosByTripId(String tripId) {
            if (photoError != null) {
                throw photoError;
            }
            List<Photo> result = new ArrayList<>();
            for (Trip t : trips) {
                if (t.getId().equals(tripId)) {
                    t.getPhotoIds().forEach(id -> result.add(photos.get(id)));
                }
            }
            return result;
        }

        // Not used by List Trips.
        @Override public Trip getTripById(String id) { throw new UnsupportedOperationException(); }
        @Override public void writeTrip(Trip trip) { throw new UnsupportedOperationException(); }
        @Override public void writeTrips(List<Trip> toWrite) { throw new UnsupportedOperationException(); }
        @Override public void deleteTripById(String id) { throw new UnsupportedOperationException(); }
        @Override public Photo getPhotoById(String id) { throw new UnsupportedOperationException(); }
        @Override public List<Photo> getPhotos() { throw new UnsupportedOperationException(); }
        @Override public void writePhoto(Photo photo) { throw new UnsupportedOperationException(); }
        @Override public void writePhotos(List<Photo> toWrite) { throw new UnsupportedOperationException(); }
    }

    
    private static final LocationInfo MONTREAL = new LocationInfo("Montreal", null, "Quebec", "Canada", "North America");
    private static final LocationInfo QUEBEC = new LocationInfo("Quebec City", null, "Quebec", "Canada", "North America");

    private FakeDataAccess dao;
    private CapturingPresenter presenter;
    private ManipulateTripsInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new FakeDataAccess();
        presenter = new CapturingPresenter();
        interactor = new ManipulateTripsInteractor(dao, dao, presenter, () -> "unused");
    }

    private static Photo photo(String id, LocalDateTime time, LocationInfo info) {
        return new Photo(id, null, info == null ? null : new Location(45, -73, info), time);
    }

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2025, 5, d, 12, 0);
    }

    @Test
    void tripsAreSortedNewestFirst() {
        dao.addTrip(new Trip("old", "Old", null, null), photo("a", day(1), MONTREAL));
        dao.addTrip(new Trip("new", "New", null, null), photo("b", day(20), MONTREAL));
        dao.addTrip(new Trip("mid", "Mid", null, null), photo("c", day(10), MONTREAL));

        interactor.listTrips();

        List<String> ids = new ArrayList<>();
        presenter.tripList.getTrips().forEach(t -> ids.add(t.getId()));
        assertEquals(List.of("new", "mid", "old"), ids);
    }

    @Test
    void tripWithoutDatesGoesLast() {
        dao.addTrip(new Trip("undated", "Undated", null, null), photo("a", null, MONTREAL));
        dao.addTrip(new Trip("dated", "Dated", null, null), photo("b", day(1), MONTREAL));

        interactor.listTrips();

        assertEquals("undated", presenter.tripList.getTrips().get(1).getId());
    }

    @Test
    void summaryHasCountsCoverAndMostCommonLocation() {
        dao.addTrip(new Trip("t", "Trip", null, null),
                photo("late", day(3), QUEBEC),
                photo("early", day(1), MONTREAL),
                photo("mid", day(2), MONTREAL));

        interactor.listTrips();

        TripSummary summary = presenter.tripList.getTrips().get(0);
        assertEquals(3, summary.getPhotoCount());
        assertEquals("Montreal, Canada", summary.getLocationLabel());
        assertEquals("early", summary.getCoverPhotoId());   // earliest photo, not first added
        assertEquals(day(1), summary.getStartTime());
        assertEquals(day(3), summary.getEndTime());
    }

    @Test
    void locationTieGoesToEarliestPhoto() {
        dao.addTrip(new Trip("t", "Trip", null, null),
                photo("a", day(1), QUEBEC),
                photo("b", day(2), MONTREAL));

        interactor.listTrips();

        assertEquals("Quebec City, Canada", presenter.tripList.getTrips().get(0).getLocationLabel());
    }

    @Test
    void photosWithoutLocationGiveNullLabel() {
        dao.addTrip(new Trip("t", "Trip", null, null), photo("a", day(1), null));

        interactor.listTrips();

        assertNull(presenter.tripList.getTrips().get(0).getLocationLabel());
    }

    @Test
    void tripWithNoPhotosHasNoCover() {
        dao.addTrip(new Trip("t", "Empty", day(1), day(1)));

        interactor.listTrips();

        TripSummary summary = presenter.tripList.getTrips().get(0);
        assertEquals(0, summary.getPhotoCount());
        assertNull(summary.getCoverPhotoId());
    }

    @Test
    void emptyArchiveSucceedsWithEmptyList() {
        interactor.listTrips();

        assertNull(presenter.tripListFailure);
        assertTrue(presenter.tripList.getTrips().isEmpty());
    }

    @Test
    void tripAccessErrorGoesToFailView() {
        dao.tripError = new TripAccessException("disk on fire");

        interactor.listTrips();

        assertNull(presenter.tripList);
        assertTrue(presenter.tripListFailure.contains("disk on fire"));
    }

    @Test
    void photoAccessErrorGoesToFailView() {
        dao.addTrip(new Trip("t", "Trip", null, null), photo("a", day(1), MONTREAL));
        dao.photoError = new PhotoAccessException("photos gone");

        interactor.listTrips();

        assertNull(presenter.tripList);
        assertTrue(presenter.tripListFailure.contains("photos gone"));
    }

    @Test
    void programmingErrorsAreNotHiddenAsFailView() {
        dao.tripError = new IllegalStateException("bug");

        assertThrows(IllegalStateException.class, () -> interactor.listTrips());
        assertNull(presenter.tripListFailure);
    }
}