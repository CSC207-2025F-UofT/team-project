package data_access;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import data_access.image.ImageResolver;
import entity.Photo;
import entity.Trip;
import use_case.PhotoAccessException;
import use_case.TripAccessException;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryDataAcessObjectTest {
    private static final LocalDateTime T0 = LocalDateTime.of(2025, 1, 1, 12, 0);
    private InMemoryDataAccessObject dao;

    @BeforeEach
    void setUp() {
        dao = new InMemoryDataAccessObject();
    }

    @Test
    void writeThenGetTrip() {
        Trip trip = new Trip("t1", "Trip", T0, T0);
        dao.writeTrip(trip);
        assertSame(trip, dao.getTripById("t1"));
    }

    @Test
    void writeThenGetPhoto() {
        Photo photo = new Photo("p1", null, null, T0);
        dao.writePhoto(photo);
        assertSame(photo, dao.getPhotoById("p1"));
    }

    @Test
    void missingIdsThrow() {
        assertThrows(TripAccessException.class, () -> dao.getTripById("nope"));
        assertThrows(PhotoAccessException.class, () -> dao.getPhotoById("nope"));
    }

    @Test
    void unknownTripHasNoPhotos() {
        assertTrue(dao.getPhotosByTripId("nope").isEmpty());
    }

    @Test
    void writeTripsAndWritePhotosStoreEverything() {
        dao.writeTrips(List.of(new Trip("a", "A", T0, T0), new Trip("b", "B", T0, T0)));
        dao.writePhotos(List.of(new Photo("p1", null, null, T0), new Photo("p2", null, null, T0)));

        assertEquals(2, dao.getTrips().size());
        assertEquals(2, dao.getPhotos().size());
    }

    @Test
    void writingSameIdReplaces() {
        dao.writeTrip(new Trip("t1", "Old name", T0, T0));
        Trip renamed = new Trip("t1", "New name", T0, T0);
        dao.writeTrip(renamed);

        assertEquals(1, dao.getTrips().size());
        assertSame(renamed, dao.getTripById("t1"));
    }

    @Test
    void deleteTripKeepsPhotos() {
        Photo p = new Photo("p1", null, null, T0);
        dao.writePhoto(p);
        Trip trip = new Trip("t1", "Trip", null, null);
        trip.addPhoto(p);
        dao.writeTrip(trip);

        dao.deleteTripById("t1");

        assertThrows(TripAccessException.class, () -> dao.getTripById("t1"));
        assertSame(p, dao.getPhotoById("p1"));
    }

    @Test 
    void deletingMissingTripThrows() {
        assertThrows(TripAccessException.class, () -> dao.deleteTripById("nope"));
    }

    @Test
    void getTripsReturnsCopyInInsertionOrder() {
        dao.writeTrip(new Trip("a", "A", T0, T0));
        dao.writeTrip(new Trip("b", "B", T0, T0));

        List<Trip> all = dao.getTrips();
        assertEquals("a", all.get(0).getId());
        assertEquals("b", all.get(1).getId());

        all.clear();
        assertEquals(2, dao.getTrips().size());
    }

    @Test
    void getPhotosByTripIdSkipsUnknownIds() {
        Photo known = new Photo("p1", null, null, T0);
        dao.writePhoto(known);
        Trip trip = new Trip("t1", "Trip", null, null);
        trip.addPhoto(known);
        trip.addPhoto(new Photo("ghost", null, null, T0));
        dao.writeTrip(trip);

        assertEquals(List.of(known), dao.getPhotosByTripId("t1"));
    }

    @Test
    void sampleDataBuildsConsistentTripsAndCopiesImages(@TempDir Path tempDir) {
        StorageResolver storage = new StorageResolver();
        storage.setDirectory(tempDir);
        ImageResolver resolver = new ImageResolver(storage);

        SampleData.populate(dao, dao, resolver);

        assertEquals(5, dao.getTrips().size());
        for (Trip trip : dao.getTrips()) {
            List<Photo> photos = dao.getPhotosByTripId(trip.getId());
            assertFalse(photos.isEmpty());
            assertEquals(photos.get(0).getTime(), trip.getStartTime());
            assertEquals(photos.get(photos.size() - 1).getTime(), trip.getEndTime());
            for (Photo photo : photos) {
                assertTrue(Files.exists(resolver.getImagePath(photo)), "missing image for " + photo.getId());
            }
        }
    }
}