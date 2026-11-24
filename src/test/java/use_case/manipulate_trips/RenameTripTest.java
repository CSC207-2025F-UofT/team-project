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

/** Tests ManipulateTripsInteractor.renameTrip(). */
class RenameTripTest {

    private InMemoryDataAccessObject dao;
    private CapturingPresenter presenter;
    private ManipulateTripsInteractor interactor;

    @BeforeEach
    void setUp() {
        dao = new InMemoryDataAccessObject();
        presenter = new CapturingPresenter();
        interactor = new ManipulateTripsInteractor(dao, dao, presenter, () -> "unused");

        Trip trip = new Trip("t", "Old Name", null, null);
        Photo photo = new Photo("p1", null, null, LocalDateTime.of(2025, 5, 1, 12, 0));
        dao.writePhoto(photo);
        trip.addPhoto(photo);
        dao.writeTrip(trip);
    }

    @Test
    void renameSavesTrimmedNameAndKeepsPhotos() {
        interactor.renameTrip(new RenameTripInputData("t", "  New Name  "));

        assertEquals("New Name", dao.getTripById("t").getName());
        assertEquals(List.of("p1"), dao.getTripById("t").getPhotoIds());
        assertNull(presenter.renameFailure);
        assertEquals("New Name", presenter.rename.getName());
        assertEquals(1, presenter.rename.getPhotos().size());
    }

    @Test
    void blankNameIsRejected() {
        interactor.renameTrip(new RenameTripInputData("t", "   "));

        assertEquals(ManipulateTripsInteractor.MISSING_RENAME, presenter.renameFailure);
        assertEquals("Old Name", dao.getTripById("t").getName());
    }

    @Test
    void unknownTripGoesToFailView() {
        interactor.renameTrip(new RenameTripInputData("missing", "X"));

        assertNull(presenter.rename);
        assertTrue(presenter.renameFailure.startsWith("Couldn't rename trip:"));
    }

    @Test
    void writeErrorGoesToFailView() {
        InMemoryDataAccessObject readOnly = new InMemoryDataAccessObject() {
            boolean locked;
            @Override public void writeTrip(Trip trip) {
                if (locked) throw new TripAccessException("read-only");
                super.writeTrip(trip);
                locked = true;   // the first write is the test setup; every later write fails
            }
        };
        readOnly.writeTrip(new Trip("t", "Old Name", null, null));

        new ManipulateTripsInteractor(readOnly, readOnly, presenter, () -> "unused")
                .renameTrip(new RenameTripInputData("t", "New Name"));

        assertTrue(presenter.renameFailure.contains("read-only"));
    }
}