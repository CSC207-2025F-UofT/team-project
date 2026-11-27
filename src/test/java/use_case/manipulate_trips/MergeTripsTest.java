package use_case.manipulate_trips;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import data_access.InMemoryDataAccessObject;
import entity.Photo;
import entity.Trip;

import static org.junit.jupiter.api.Assertions.*;

/** Tests ManipulateTripsInteractor.mergeTrips(). */
class MergeTripsTest {

    private InMemoryDataAccessObject dao;
    private CapturingPresenter presenter;
    private ManipulateTripsInteractor interactor;

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

    @BeforeEach
    void setUp() {
        dao = new InMemoryDataAccessObject();
        presenter = new CapturingPresenter();
        interactor = new ManipulateTripsInteractor(dao, dao, presenter, () -> "unused");

        addTrip("later", "Later", new Photo("c", null, null, day(10)), new Photo("d", null, null, day(12)));
        addTrip("earlier", "Earlier", new Photo("a", null, null, day(1)), new Photo("b", null, null, day(11)));
        addTrip("untouched", "Untouched", new Photo("e", null, null, day(20)));
    }

    @Test
    void earliestTripSurvivesWithAllPhotosInOrder() {
        interactor.mergeTrips(new MergeTripsInputData(List.of("later", "earlier")));

        Trip merged = dao.getTripById("earlier");
        assertEquals("Earlier", merged.getName());
        assertEquals(List.of("a", "c", "b", "d"), merged.getPhotoIds());
        assertEquals(day(1), merged.getStartTime());
        assertEquals(day(12), merged.getEndTime());
    }

    @Test
    void otherTripsAreDeletedAndUnrelatedTripsKept() {
        interactor.mergeTrips(new MergeTripsInputData(List.of("later", "earlier")));

        List<String> remaining = dao.getTrips().stream().map(Trip::getId).collect(Collectors.toList());
        assertEquals(2, remaining.size());
        assertTrue(remaining.contains("earlier"));
        assertTrue(remaining.contains("untouched"));
    }

    @Test
    void successReportsMergedTripAndCount() {
        interactor.mergeTrips(new MergeTripsInputData(List.of("earlier", "later", "untouched")));

        assertNull(presenter.mergeFailure);
        assertEquals(3, presenter.merge.getMergedCount());
        assertEquals("earlier", presenter.merge.getMergedTrip().getTripId());
        assertEquals(List.of("a", "c", "b", "d", "e"), presenter.merge.getMergedTrip().getPhotos().stream()
                .map(PhotoDetails::getPhotoId).collect(Collectors.toList()));
        assertTrue(presenter.merge.getMergedTrip().getOtherTrips().isEmpty());
    }

    @Test
    void fewerThanTwoDistinctTripsIsRejected() {
        interactor.mergeTrips(new MergeTripsInputData(List.of("earlier", "earlier")));

        assertEquals(ManipulateTripsInteractor.TOO_FEW_TRIPS, presenter.mergeFailure);
        assertEquals(3, dao.getTrips().size());
    }

    @Test
    void unknownTripFailsWithoutChangingAnything() {
        interactor.mergeTrips(new MergeTripsInputData(List.of("earlier", "missing")));

        assertNull(presenter.merge);
        assertTrue(presenter.mergeFailure.startsWith("Couldn't merge trips:"));
        assertEquals(3, dao.getTrips().size());
        assertEquals(List.of("a", "b"), dao.getTripById("earlier").getPhotoIds());
    }
}