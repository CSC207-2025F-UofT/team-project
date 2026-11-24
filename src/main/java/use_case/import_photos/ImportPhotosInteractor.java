package use_case.import_photos;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.stream.Stream;

import entity.Photo;
import entity.Location;
import entity.LocationInfo;
import entity.LocationInfoType;
import entity.Trip;
import use_case.PhotoAccessException;
import use_case.PhotoAccessInterface;
import use_case.TripAccessException;
import use_case.TripAccessInterface;

public class ImportPhotosInteractor implements ImportPhotosInputBoundary {
    private static final float TRIP_END_TIME_THRESHOLD = 48;
    private static final float TRIP_END_DISTANCE_THRESHOLD = 6672;

    private final ImageImporterInterface imageImporter;
    private final GeocoderInterface geocoder;
    private final ReverseGeocoderInterface reverseGeocoder;
    private final PhotoAccessInterface photoAccess;
    private final TripAccessInterface tripAccess;
    private final ImportPhotosOutputBoundary presenter;

    private static final List<LocationInfoType> INFO_TYPE_ORDER = List.of(
        LocationInfoType.CITY, LocationInfoType.COUNTY, LocationInfoType.STATE,
        LocationInfoType.COUNTRY, LocationInfoType.CONTINENT
    );

    private static LocationInfoType getFirstCommonInfoType(List<LocationInfo> infoList) {
        for (LocationInfoType infoType : INFO_TYPE_ORDER) {
            String commonInfo = null;

            for (LocationInfo locationInfo : infoList) {
                if (locationInfo == null) {
                    commonInfo = null;
                    break;
                }

                String info = locationInfo.getType(infoType);

                if (info == null) {
                    commonInfo = null;
                    break;
                }

                if (commonInfo == null) {
                    commonInfo = info;
                    continue;
                }

                if (!commonInfo.equals(info)) {
                    commonInfo = null;
                    break;
                }
            }

            if (commonInfo != null) {
                return infoType;
            }
        }

        return null;
    }

    public ImportPhotosInteractor(
        ImageImporterInterface imageImporter, GeocoderInterface geocoder,
        ReverseGeocoderInterface reverseGeocoder, PhotoAccessInterface photoAccess,
        TripAccessInterface tripAccess, ImportPhotosOutputBoundary presenter
    ) {
        this.imageImporter = imageImporter;
        this.geocoder = geocoder;
        this.reverseGeocoder = reverseGeocoder;
        this.photoAccess = photoAccess;
        this.tripAccess = tripAccess;
        this.presenter = presenter;
    }

    private List<Photo> getImportedPhotos(List<String> paths) {
        AtomicInteger successes = new AtomicInteger(0);
        List<String> failedPaths = new ArrayList<>();

        presenter.updateImportView(new ImportPhotosImportOutputData());

        List<Photo> imported = imageImporter.importImages(paths, new Consumer<ImageImportProgressData>() {
            @Override
            public void accept(ImageImportProgressData progressData) {
                if (progressData.getSuccess()) {
                    successes.incrementAndGet();
                } else {
                    failedPaths.add(progressData.getImagePath());
                }

                presenter.updateImportView(new ImportPhotosImportOutputData(
                    successes.get(), failedPaths, progressData.getTotalImages()
                ));
            }
        });

        presenter.updateImportView(new ImportPhotosImportOutputData(
            successes.get(), failedPaths, imported.size()
        ));

        return imported;
    }

    private void reverseGeocodePhotos(List<Photo> photos) {
        presenter.updateGeocodeView(new ImportPhotosGeocodeOutputData());

        int successes = 0;
        int failures = 0;
        int skipped = 0;

        for (Photo photo : photos) {
            Location location = photo.getLocation();
            LocationInfo locationInfo = null;

            if (location == null) {
                skipped += 1;

            } else {
                try {
                    locationInfo = reverseGeocoder.getLocationInfo(
                        location.getLatitude(), location.getLongitude()
                    );

                    successes += 1;

                } catch (ReverseGeocodeException e) {
                    failures += 1;
                }
            }

            if (locationInfo != null) {
                location.setInfo(locationInfo);
            }

            presenter.updateGeocodeView(new ImportPhotosGeocodeOutputData(successes, failures, skipped));
        }
    }

    private List<Photo> getSortedTripEligiblePhotos(List<Photo> photos, Location exLocation, int exDistance) {
        presenter.preparePhotoSortingView();

        Stream<Photo> includedPhotoStream = photos.stream()
            .filter(photo -> photo.getLocation() != null && photo.getTime() != null);

        if (exLocation != null) {
            includedPhotoStream = includedPhotoStream
                .filter(p -> p.getLocation().getDistanceFrom(exLocation) > exDistance);
        }

        return includedPhotoStream.sorted().toList();
    }

    private String getTripLocationLabel(List<Photo> tripPhotos) {
        List<LocationInfo> tripLocationInfo = tripPhotos.stream()
            .map(Photo::getLocation).map(Location::getInfo).toList();

        LocationInfoType commonInfoType = getFirstCommonInfoType(tripLocationInfo);

        return commonInfoType != null ? tripLocationInfo.getFirst().getType(commonInfoType) : null;
    }

    private String getTripTimeSpanLabel(List<Photo> tripPhotos) {
        LocalDateTime start = tripPhotos.getFirst().getTime();
        LocalDateTime end = tripPhotos.getLast().getTime();

        if (start.getYear() == end.getYear()) {
            String startMonth = start.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
            String endMonth = end.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);

            return start.getMonth() == end.getMonth() ?
                startMonth + " " + start.getYear() :
                startMonth + " - " + endMonth + " " + start.getYear();
        }

        return start.getYear() + " - " + end.getYear();
    }

    private String generateTripName(List<Photo> tripPhotos) {
        if (tripPhotos.isEmpty()) {
            return "Trip";
        }

        String locationStr = getTripLocationLabel(tripPhotos);
        String timeStr = getTripTimeSpanLabel(tripPhotos);

        String base = locationStr != null ? "Trip to " + locationStr : "Trip";

        return timeStr != null ? base + " " + timeStr : base;
    }

    private Trip generateTrip(List<Photo> tripPhotos) {
        return new Trip(
            UUID.randomUUID().toString(),
            generateTripName(tripPhotos),
            tripPhotos.getFirst().getTime(),
            tripPhotos.getLast().getTime(),
            tripPhotos.stream().map(Photo::getId).toList()
        );
    }

    private List<Trip> generateTrips(List<Photo> orderedEligiblePhotos) {
        presenter.prepareTripGenerationView();

        List<Trip> trips = new ArrayList<>();
        List<Photo> currentTripPhotos = new ArrayList<>();

        for (int i = 0; i < orderedEligiblePhotos.size(); i++) {
            Photo photo = orderedEligiblePhotos.get(i);

            if (currentTripPhotos.isEmpty()) {
                currentTripPhotos.add(photo);

                continue;
            }

            Photo lastPhoto = currentTripPhotos.getLast();
            Duration timeDifference = lastPhoto.getTimeTo(photo);
            float hoursDifference = timeDifference.toSeconds() / (float) TimeUnit.HOURS.toSeconds(1);
            double distance = lastPhoto.getLocation().getDistanceFrom(photo.getLocation());

            if (
                hoursDifference > TRIP_END_TIME_THRESHOLD ||
                distance > TRIP_END_DISTANCE_THRESHOLD
            ) {
                trips.add(generateTrip(currentTripPhotos));
                currentTripPhotos.clear();
            }

            currentTripPhotos.add(photo);
        }

        if (!currentTripPhotos.isEmpty()) {
            trips.add(generateTrip(currentTripPhotos));
        }

        return trips;
    }

    public void execute(ImportPhotosInputData inputData) {
        String exLocationDesc = inputData.getExcludeLocationDesc();
        int exDistance = inputData.getExcludeDistance();
        Location exLocation = null;

        if (exLocationDesc != null) {
            try {
                exLocation = geocoder.getLocation(exLocationDesc);

            } catch (GeocodeException e) {
                presenter.prepareInvalidExclusionView(e.getMessage());

                return;
            }
        }

        try {
            List<Photo> photos = getImportedPhotos(inputData.getPaths());
            reverseGeocodePhotos(photos);

            try {
                photoAccess.writePhotos(photos);

            } catch (PhotoAccessException e) {
                imageImporter.cleanupImages(photos);
                presenter.preparePhotoSaveFailureView(e.getMessage());

                return;
            }

            List<Photo> tripEligiblePhotos = getSortedTripEligiblePhotos(photos, exLocation, exDistance);
            List<Trip> generatedTrips = generateTrips(tripEligiblePhotos);

            try {
                tripAccess.writeTrips(generatedTrips);

                presenter.prepareTripSaveSuccessView(new ImportPhotosTripOutputData(
                    generatedTrips.stream().mapToInt(t -> t.getPhotoIds().size()).sum(),
                    generatedTrips.stream().map(t -> t.getName()).toList()
                ));

            } catch (TripAccessException e) {
                presenter.prepareTripSaveFailureView(e.getMessage());
            }

        } catch (ImageImportException e) {
            presenter.prepareImportFailureView(e.getMessage());
        }
    }
}
