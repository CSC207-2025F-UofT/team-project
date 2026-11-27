package use_case.import_photos;

import java.time.LocalDateTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.ImageType;
import entity.Location;
import entity.LocationInfo;
import entity.Photo;
import entity.Trip;
import use_case.IdGeneratorInterface;
import data_access.InMemoryDataAccessObject;

class ImportPhotosInteractorTest {
    private static class MockImageImporter implements ImageImporterInterface {
        private String failPath = null;
        private Queue<Photo> metadata = new ArrayDeque<>();
        private Set<String> savedImages = new HashSet<>();

        @Override
        public List<Photo> importImages(List<String> paths, Consumer<ImageImportProgressData> onImageProcessed) {
            List<Photo> photos = new ArrayList<>();

            for (String path : paths) {
                if (failPath != null && path.equals(failPath)) {
                    throw new ImageImportException(path, "Fail");
                }

                Photo photo = metadata.poll();
                boolean successful = photo != null;

                if (successful) {
                    photos.add(photo);
                    savedImages.add(photo.getId());
                }

                onImageProcessed.accept(new ImageImportProgressData(path, successful, paths.size()));
            }

            return photos;
        }

        @Override
        public void cleanupImages(List<Photo> photos) {
            for (Photo photo : photos) {
                savedImages.remove(photo.getId());
            }
        }
    }

    private static class RecordingPresenter implements ImportPhotosOutputBoundary {
        private List<ImportPhotosImportOutputData> importData = new ArrayList<>();
        private List<ImportPhotosGeocodeOutputData> geocodeData = new ArrayList<>();
        private List<ImportPhotosTripOutputData> tripData = new ArrayList<>();
        private List<String> errors = new ArrayList<>();
        private int photoSavingCount = 0;
        private int tripGenerationCount = 0;
        private int tripSavingCount = 0;

        @Override
        public void prepareInvalidExclusionView(String error) { errors.add(error); }
        @Override
        public void prepareImportFailureView(String error) { errors.add(error); }
        @Override
        public void updateImportView(ImportPhotosImportOutputData outputData) { importData.add(outputData); }
        @Override
        public void updateGeocodeView(ImportPhotosGeocodeOutputData outputData) { geocodeData.add(outputData); }
        @Override
        public void preparePhotoSavingView() { photoSavingCount++; }
        @Override
        public void preparePhotoSaveFailureView(String error) { errors.add(error); }
        @Override
        public void prepareTripGenerationView() { tripGenerationCount++; }
        @Override
        public void prepareTripSavingView() { tripSavingCount++; }
        @Override
        public void prepareTripSaveFailureView(String error) { errors.add(error); }
        @Override
        public void prepareTripSaveSuccessView(ImportPhotosTripOutputData outputData) { tripData.add(outputData); }
    }

    private static class MockGeocoder implements GeocoderInterface, ReverseGeocoderInterface {
        private boolean geocodeFailMode = false;
        private boolean reverseGeocodeFailMode = false;
        private Queue<Location> locations = new ArrayDeque<>();
        private Queue<LocationInfo> locationInfos = new ArrayDeque<>();

        @Override
        public Location getLocation(String description) {
            if (geocodeFailMode) throw new GeocodeException("Geocoder failed");

            return locations.poll();
        }

        @Override
        public LocationInfo getLocationInfo(double latitude, double longitude) {
            if (reverseGeocodeFailMode) throw new ReverseGeocodeException("Reverse geocoder failed");

            return locationInfos.poll();
        }
    }

    private static class TestIdGenerator implements IdGeneratorInterface {
        private long count = 0;

        @Override
        public String nextId() { return "id" + String.valueOf(++count); }
    }

    private final LocationInfo info1 = new LocationInfo("Toronto", null, "Ontario", "Canada", "North America");
    private final LocationInfo info2 = new LocationInfo("Toronto", null, "Ontario", "Canada", "North America");
    private final LocationInfo info3 = new LocationInfo("Mississauga", null, "Ontario", "Canada", "North America");
    private final LocationInfo info4 = new LocationInfo("Lyon", "Rhone", "Auvergne-Rhone-Alpes", "France", "Europe");
    private final LocationInfo info5 = new LocationInfo("Lyon", "Rhone", "Auvergne-Rhone-Alpes", "France", "Europe");
    private final LocationInfo info6 = new LocationInfo("Villeurbanne", "Rhone", "Auvergne-Rhone-Alpes", "France", "Europe");
    private final LocationInfo info7 = new LocationInfo("Villeurbanne", "Rhone", "Auvergne-Rhone-Alpes", "France", "Europe");
    private final LocationInfo info8 = new LocationInfo("Villeurbanne", "Rhone", "Auvergne-Rhone-Alpes", "France", "Europe");

    private final LocalDateTime time1 = LocalDateTime.of(2026, 11, 25, 9, 0, 0);
    private final LocalDateTime time2 = LocalDateTime.of(2026, 11, 25, 11, 30, 0);
    private final LocalDateTime time3 = LocalDateTime.of(2026, 11, 25, 14, 0, 0);
    private final LocalDateTime time4 = LocalDateTime.of(2026, 11, 25, 16, 0, 0);
    private final LocalDateTime time5 = LocalDateTime.of(2026, 11, 25, 18, 0, 0);
    private final LocalDateTime time6 = LocalDateTime.of(2026, 11, 25, 20, 0, 0);
    private final LocalDateTime time7 = LocalDateTime.of(2026, 12, 1, 20, 0, 0);
    private final LocalDateTime time8 = LocalDateTime.of(2026, 12, 2, 21, 30, 0);

    private MockImageImporter imageImporter;
    private InMemoryDataAccessObject dataAccessObject;
    private MockGeocoder geocoder;
    private RecordingPresenter presenter;
    private TestIdGenerator idGenerator;
    private ImportPhotosInteractor interactor;

    private Photo getPhoto(String id) {
        return new Photo(id, ImageType.JPEG, new Location(0, 0), LocalDateTime.now());
    }

    private List<Photo> getPhotos(String... ids) {
        return Arrays.stream(ids).map(this::getPhoto).toList();
    }

    private Photo getBlankPhoto(String id) {
        return new Photo(id, ImageType.JPEG, null, null);
    }

    private List<Photo> getBlankPhotos(String... ids) {
        return Arrays.stream(ids).map(this::getBlankPhoto).toList();
    }

    @BeforeEach
    void setup() {
        imageImporter = new MockImageImporter();
        dataAccessObject = new InMemoryDataAccessObject();
        geocoder = new MockGeocoder();
        presenter = new RecordingPresenter();
        idGenerator = new TestIdGenerator();
        interactor = new ImportPhotosInteractor(
            imageImporter, geocoder, geocoder, dataAccessObject, dataAccessObject, idGenerator, presenter
        );
    }

    @Test
    void geocodeFail() {
        geocoder.geocodeFailMode = true;
        interactor.execute(new ImportPhotosInputData("", 0, List.of()));

        assertEquals(1, presenter.errors.size());
        assertEquals("Geocoder failed", presenter.errors.getFirst());
    }

    @Test
    void geocodeNoResults() {
        interactor.execute(new ImportPhotosInputData("", 0, List.of()));

        assertEquals(1, presenter.errors.size());
        assertEquals("No results found", presenter.errors.getFirst());
    }

    @Test
    void importPathFail() {
        imageImporter.failPath = "path2";
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("path1", "path2", "path3")));

        assertEquals(1, presenter.errors.size());
        assertEquals("Import failed for path \"path2\": Fail", presenter.errors.getFirst());
    }

    @Test
    void importPathSkip() {
        imageImporter.metadata = new LinkedList<>(Arrays.asList(getPhoto("id1"), null, getPhoto("id3")));
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("p1", "p2", "p3")));

        assertEquals(5, presenter.importData.size());
        assertEquals(1, presenter.importData.get(1).getSuccesses());
        assertEquals(2, presenter.importData.getLast().getSuccesses());
        assertEquals(List.of("p2"), presenter.importData.getLast().getFailedPaths());
    }

    @Test
    void importImageSave() {
        imageImporter.metadata = new ArrayDeque<>(getPhotos("id1", "id2"));
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("p1", "p2")));

        assertEquals(Set.of("id1", "id2"), imageImporter.savedImages);
    }

    @Test
    void reverseGeocodeSkip() {
        imageImporter.metadata = new ArrayDeque<>(
            Stream.concat(getBlankPhotos("id1").stream(), getPhotos("id2", "id3").stream()).toList()
        );
        geocoder.locationInfos = new ArrayDeque<>(List.of(info2, info3));
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("p1", "p2", "p3")));

        assertEquals(4, presenter.geocodeData.size());
        assertEquals(0, presenter.geocodeData.get(1).getGeocodedCount());
        assertEquals(2, presenter.geocodeData.getLast().getGeocodedCount());
        assertEquals(1, presenter.geocodeData.getLast().getSkipCount());
    }

    @Test
    void reverseGeocodeFail() {
        imageImporter.metadata = new ArrayDeque<>(getPhotos("id1", "id2"));
        geocoder.reverseGeocodeFailMode = true;
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("p1", "p2")));

        assertEquals(0, presenter.geocodeData.getLast().getGeocodedCount());
        assertEquals(2, presenter.geocodeData.getLast().getFailCount());
    }

    @Test
    void photoWriteFail() {
        imageImporter.metadata = new ArrayDeque<>(getPhotos("id1", "id2"));
        dataAccessObject.setPhotoFailMode(true);
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("p1", "p2")));

        assertEquals(0, imageImporter.savedImages.size());
        assertEquals(1, presenter.errors.size());
        assertEquals("Photo access failed", presenter.errors.getLast());
    }

    @Test
    void photoWriteSuccess() {
        imageImporter.metadata = new ArrayDeque<>(getPhotos("id1", "id2"));
        geocoder.locationInfos = new ArrayDeque<>(List.of(info1, info2));
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("p1", "p2")));

        assertEquals(info1, dataAccessObject.getPhotoById("id1").getLocation().getInfo());
        assertEquals(info2, dataAccessObject.getPhotoById("id2").getLocation().getInfo());
        assertEquals(1, presenter.photoSavingCount);
    }

    @Test
    void fullCompletion() {
        interactor.execute(new ImportPhotosInputData(null, 0, List.of()));

        assertEquals(1, presenter.tripGenerationCount);
        assertEquals(1, presenter.tripSavingCount);
        assertEquals(1, presenter.tripData.size());
    }

    @Test
    void tripSaveFailure() {
        dataAccessObject.setTripFailMode(true);
        interactor.execute(new ImportPhotosInputData(null, 0, List.of()));

        assertEquals(1, presenter.errors.size());
        assertEquals("Trip access failed", presenter.errors.getLast());
    }

    @Test
    void tripGenerationSkippedPhotos() {
        imageImporter.metadata = new LinkedList<>(Arrays.asList(
            getBlankPhoto("id1"), getPhoto("id2"), getBlankPhoto("id3"), getPhoto("id4")
        ));
        interactor.execute(new ImportPhotosInputData(null, 0, List.of("p1", "p2", "p3", "p4")));

        assertEquals(2, presenter.tripData.getLast().getSortedPhotoCount());
        assertEquals(
            List.of("id2", "id4"),
            dataAccessObject.getTripById("id1").getPhotoIds().stream().sorted().toList()
        );
    }

    @Test
    void tripGenerationManyPhotos() {
        imageImporter.metadata = new ArrayDeque<>(List.of(
            new Photo("id1", ImageType.JPEG, new Location(43.6426, -79.3871), time1),
            new Photo("id3", ImageType.WEBP, new Location(43.6550, -79.4250), time3),
            new Photo("id5", ImageType.PNG, new Location(45.7578, 4.8320), time5),
            new Photo("id7", ImageType.JPEG, new Location(45.7620, 4.8680), time7),
            new Photo("id2", ImageType.PNG, new Location(43.6480, -79.4050), time2),
            new Photo("id4", ImageType.JPEG, new Location(45.7640, 4.8357), time4),
            new Photo("id6", ImageType.WEBP, new Location(45.7667, 4.8794), time6),
            new Photo("id8", ImageType.PNG, new Location(45.7550, 4.8400), time8)
        ));
        geocoder.locationInfos = new ArrayDeque<>(List.of(
            info1, info3, info5, info7, info2, info4, info6, info8
        ));
        interactor.execute(new ImportPhotosInputData(
            null, 0, List.of("p1", "p3", "p5", "p7", "p2", "p4", "p6", "p8")
        ));

        Trip trip1 = dataAccessObject.getTripById("id1");
        Trip trip2 = dataAccessObject.getTripById("id2");
        Trip trip3 = dataAccessObject.getTripById("id3");

        assertEquals(3, dataAccessObject.getTrips().size());
        assertEquals("Trip to Ontario Nov 2026", trip1.getName());
        assertEquals("Trip to Rhone Nov 2026", trip2.getName());
        assertEquals("Trip to Villeurbanne Dec 2026", trip3.getName());
        assertEquals(List.of("id1", "id2", "id3"), trip1.getPhotoIds().stream().sorted().toList());
        assertEquals(List.of("id4", "id5", "id6"), trip2.getPhotoIds().stream().sorted().toList());
        assertEquals(List.of("id7", "id8"), trip3.getPhotoIds().stream().sorted().toList());
    }

    @Test
    void tripGenerationExclusion() {
        imageImporter.metadata = new ArrayDeque<>(List.of(
            new Photo("id1", ImageType.JPEG, new Location(43.6426, -79.3871), time1),
            new Photo("id3", ImageType.WEBP, new Location(43.6550, -79.4250), time3),
            new Photo("id5", ImageType.PNG, new Location(45.7578, 4.8320), time5),
            new Photo("id7", ImageType.JPEG, new Location(45.7620, 4.8680), time7),
            new Photo("id2", ImageType.PNG, new Location(43.6480, -79.4050), time2),
            new Photo("id4", ImageType.JPEG, new Location(45.7640, 4.8357), time4),
            new Photo("id6", ImageType.WEBP, new Location(45.7667, 4.8794), time6),
            new Photo("id8", ImageType.PNG, new Location(45.7550, 4.8400), time8)
        ));
        geocoder.locations = new ArrayDeque<>(List.of(new Location(43.6453, -79.3960)));
        geocoder.locationInfos = new ArrayDeque<>(List.of(
            info1, info3, info5, info7, info2, info4, info6, info8
        ));
        interactor.execute(new ImportPhotosInputData(
            "Toronto, Ontario", 1, List.of("p1", "p3", "p5", "p7", "p2", "p4", "p6", "p8")
        ));

        Trip trip1 = dataAccessObject.getTripById("id1");
        Trip trip2 = dataAccessObject.getTripById("id2");
        Trip trip3 = dataAccessObject.getTripById("id3");

        assertEquals(3, dataAccessObject.getTrips().size());
        assertEquals("Trip to Mississauga Nov 2026", trip1.getName());
        assertEquals("Trip to Rhone Nov 2026", trip2.getName());
        assertEquals("Trip to Villeurbanne Dec 2026", trip3.getName());
        assertEquals(List.of("id3"), trip1.getPhotoIds().stream().sorted().toList());
        assertEquals(List.of("id4", "id5", "id6"), trip2.getPhotoIds().stream().sorted().toList());
        assertEquals(List.of("id7", "id8"), trip3.getPhotoIds().stream().sorted().toList());
    }
}
