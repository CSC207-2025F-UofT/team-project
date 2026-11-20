package data_access;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import data_access.image.ImageResolver;
import entity.ImageType;
import entity.Location;
import entity.LocationInfo;
import entity.Photo;
import entity.Trip;
import use_case.PhotoAccessInterface;
import use_case.TripAccessInterface;

/** Seeds a DAO with demo trips so GUI dev isn't blocked on file imports */
public final class SampleData {
    private static final String[] IMAGES = {"sample1.jpg", "sample2.jpg", "sample3.jpg"};
    private static int photoCounter = 0;

    private SampleData() {}

    public static void populate(TripAccessInterface trips, PhotoAccessInterface photos, ImageResolver resolver) {
        addTrip(trips, photos, resolver, 0, "trip-montreal", "Montreal Weekend",
                LocalDateTime.of(2026, 11, 12, 10, 0), 5,
                45.5017, -73.5673, new LocationInfo("Montreal", null, "Quebec", "Canada", "North America", "landmark-montreal"));
        addTrip(trips, photos, resolver, 1, "trip-tokyo", "Tokyo",
                LocalDateTime.of(2025, 11, 12, 10, 0), 6,
                35.6762, 139.6503, new LocationInfo("Tokyo", null, "Tokyo", "Japan", "Asia", "Imperial Palace"));
        addTrip(trips, photos, resolver, 2, "trip-vancouver", "Vancouver",
                LocalDateTime.of(2025, 06, 10, 10, 0), 4,
                49.2827, -123.1207, new LocationInfo("Vancouver", null, "British Columbia", "Canada", "North America", "Stanley Park"));
        addTrip(trips, photos, resolver, 1, "trip-osaka", "Osaka Trip",
                LocalDateTime.of(2025, 11, 12, 10, 0), 6,
                35.6762, 139.6503, new LocationInfo("Osaka", null, "Osaka", "Japan", "Asia", "The Shibuya Crossing"));
        addTrip(trips, photos, resolver, 1, "trip-canton", "Guangzhou",
                LocalDateTime.of(2027, 3, 1, 10, 0), 6,
                23.10902, 113.31915, new LocationInfo("guangzhou", null, "Guangzhou", "China", "Asia", "Canton Tower"));
    }

    private static void addTrip(TripAccessInterface trips, PhotoAccessInterface photos, ImageResolver resolver,
                                int imageOffset, String id, String name, LocalDateTime start, int photoCount,
                                double lat, double lon, LocationInfo info) {
        Trip trip = new Trip(id, name, null, null);
        
        for (int i = 0; i < photoCount; i++) {
            // Spread photos ~ every 7 hours and a few hundred meters apart
            Location loc = new Location(lat + i * 0.003, lon + i * 0.004, info);
            Photo photo = new Photo("photo-" + (++photoCounter), ImageType.JPEG, loc, start.plusHours(i * 7L));

            copySampleImage(IMAGES[(imageOffset + i) % IMAGES.length], resolver.getImagePath(photo));
            photos.writePhoto(photo);
            trip.addPhoto(photo);
        }
        trips.writeTrip(trip);
    }

    /** Copies bundled sample image into app storage. */
    private static void copySampleImage(String fileName, Path target) {
        try (InputStream in = SampleData.class.getResourceAsStream("/sample/" + fileName)) {
            if (in == null) {
                return;    // resource missing -> ImageReader fails -> placeholder.
            }
            Files.createDirectories(target.getParent());
            Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            // card just shows a placeholder. whatever
        }
    }
}
