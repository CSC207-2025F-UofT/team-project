package use_case.manipulate_trips;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.InMemoryDataAccessObject;
import entity.Photo;
import entity.Trip;
import use_case.PhotoAccessException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests ManipulateTripsInteractor.viewTrip(). */
class ViewTripTest {

    private InMemoryDataAccessObject dao;
    private CapturingPresenter presenter;
    private ManipulateTripsInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new InMemoryDataAccessObject();
        presenter = new CapturingPresenter();
        interactor = new ManipulateTripsInteractor(dao, dao, presenter, () -> "unused");
    }

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2025, 5, d, 12, 0);
    }

    private void addTrip(String id, String name, Photo... photos) {
        Trip trip = new Trip(id, name, null, null);
        for (Photo p : photos) {
            dao.writePhoto(p);
            trip.addPhoto(p);
        }
        dao.writeTrip(trip);
    }

    @Test
    void photosAreChronologicalWithUndatedLast() {
        addTrip("t", "Trip",
                new Photo("undated", null, null, null),
                new Photo("late", null, null, day(3)),
                new Photo("early", null, null, day(1)));

        interactor.viewTrip(new ViewTripInputData("t"));

        List<String> ids = presenter.details.getPhotos().stream()
                .map(PhotoDetails::getPhotoId).collect(Collectors.toList());
        assertEquals(List.of("early", "late", "undated"), ids);
        assertEquals("Trip", presenter.details.getName());
        assertEquals(day(1), presenter.details.getStartTime());
        assertEquals(day(3), presenter.details.getEndTime());
    }

    @Test
    void otherTripsAreListedNewestFirstWithoutTheCurrentOne() {
        addTrip("current", "Current", new Photo("a", null, null, day(10)));
        addTrip("old", "Old", new Photo("b", null, null, day(1)));
        addTrip("new", "New", new Photo("c", null, null, day(20)));

        interactor.viewTrip(new ViewTripInputData("current"));

        List<String> ids = presenter.details.getOtherTrips().stream()
                .map(TripReference::getTripId).collect(Collectors.toList());
        assertEquals(List.of("new", "old"), ids);
    }

    @Test
    void unknownTripGoesToFailView() {
        interactor.viewTrip(new ViewTripInputData("missing"));

        assertNull(presenter.details);
        assertTrue(presenter.detailsFailure.startsWith("Couldn't open trip:"));
    }

    @Test
    void photoAccessErrorGoesToFailView() {
        addTrip("t", "Trip", new Photo("a", null, null, day(1)));
        InMemoryDataAccessObject broken = new InMemoryDataAccessObject() {
            @Override public List<Photo> getPhotosByTripId(String tripId) { throw new PhotoAccessException("photos gone"); }
        };
        broken.writeTrip(dao.getTripById("t"));

        new ManipulateTripsInteractor(broken, broken, presenter, () -> "unused").viewTrip(new ViewTripInputData("t"));

        assertNull(presenter.details);
        assertTrue(presenter.detailsFailure.contains("photos gone"));
    }
}