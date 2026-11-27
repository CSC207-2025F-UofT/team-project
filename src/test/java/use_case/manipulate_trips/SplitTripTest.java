package use_case.manipulate_trips;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.InMemoryDataAccessObject;
import entity.Photo;
import entity.Trip;
import use_case.TripAccessException;

import static org.junit.jupiter.api.Assertions.*;

/** Tests ManipulateTripsInteractor.splitTrip(). */
class SplitTripTest {

    private InMemoryDataAccessObject dao;
    private CapturingPresenter presenter;
    private ManipulateTripsInteractor interactor;

    private static LocalDateTime day(int d) {
        return LocalDateTime.of(2025, 5, d, 12, 0);
    }

    @BeforeEach
    void setUp() {
        dao = new InMemoryDataAccessObject();
        presenter = new CapturingPresenter();
        interactor = new ManipulateTripsInteractor(dao, dao, presenter, () -> "new-id");

        Trip trip = new Trip("t", "Road Trip", null, null);
        for (Photo p : List.of(new Photo("p3", null, null, day(3)), new Photo("p1", null, null, day(1)),
                new Photo("p4", null, null, day(4)), new Photo("p2", null, null, day(2)))) {
            dao.writePhoto(p);
            trip.addPhoto(p);
        }
        dao.writeTrip(trip);
    }

    @Test
    void splitMovesSelectedAndLaterPhotosToNewTrip() {
        interactor.splitTrip(new SplitTripInputData("t", "p3", "Second half"));

        Trip original = dao.getTripById("t");
        Trip created = dao.getTripById("new-id");
        assertEquals(List.of("p1", "p2"), original.getPhotoIds());
        assertEquals(List.of("p3", "p4"), created.getPhotoIds());
        assertEquals("Road Trip", original.getName());
        assertEquals("Second half", created.getName());
    }

    @Test
    void splitRecomputesStartAndEndTimes() {
        interactor.splitTrip(new SplitTripInputData("t", "p3", "Second half"));

        assertEquals(day(1), dao.getTripById("t").getStartTime());
        assertEquals(day(2), dao.getTripById("t").getEndTime());
        assertEquals(day(3), dao.getTripById("new-id").getStartTime());
        assertEquals(day(4), dao.getTripById("new-id").getEndTime());
    }

    @Test
    void successReportsOriginalTripAndNewName() {
        interactor.splitTrip(new SplitTripInputData("t", "p3", "  Second half  "));

        assertNull(presenter.splitFailure);
        assertEquals("t", presenter.split.getOriginalTrip().getTripId());
        assertEquals(2, presenter.split.getOriginalTrip().getPhotos().size());
        assertEquals("new-id", presenter.split.getNewTripId());
        assertEquals("Second half", presenter.split.getNewTripName());   // trimmed
    }

    @Test
    void splittingAtFirstPhotoIsRejected() {
        interactor.splitTrip(new SplitTripInputData("t", "p1", "Everything"));

        assertEquals(ManipulateTripsInteractor.SPLIT_AT_FIRST, presenter.splitFailure);
        assertEquals(4, dao.getTripById("t").getPhotoIds().size());
        assertEquals(1, dao.getTrips().size());
    }

    @Test
    void blankNameIsRejected() {
        interactor.splitTrip(new SplitTripInputData("t", "p3", "   "));

        assertEquals(ManipulateTripsInteractor.MISSING_NAME, presenter.splitFailure);
        assertEquals(1, dao.getTrips().size());
    }

    @Test
    void photoFromAnotherTripIsRejected() {
        interactor.splitTrip(new SplitTripInputData("t", "not-here", "X"));

        assertEquals(ManipulateTripsInteractor.PHOTO_NOT_IN_TRIP, presenter.splitFailure);
    }

    @Test
    void unknownTripGoesToFailView() {
        interactor.splitTrip(new SplitTripInputData("missing", "p3", "X"));

        assertNull(presenter.split);
        assertTrue(presenter.splitFailure.startsWith("Couldn't split trip:"));
    }

    @Test
    void writeErrorGoesToFailView() {
        InMemoryDataAccessObject readOnly = new InMemoryDataAccessObject() {
            @Override public void writeTrips(List<Trip> trips) { throw new TripAccessException("read-only"); }
        };
        readOnly.writeTrip(dao.getTripById("t"));
        dao.getPhotos().forEach(readOnly::writePhoto);

        new ManipulateTripsInteractor(readOnly, readOnly, presenter, () -> "new-id")
                .splitTrip(new SplitTripInputData("t", "p3", "X"));

        assertTrue(presenter.splitFailure.contains("read-only"));
    }
}